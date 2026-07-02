terraform {
  required_version = "1.15.5"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
    github = {
      source  = "integrations/github"
      version = "~> 6.0"
    }
  }
}

# Configuración del proveedor de GitHub
provider "github" {
  owner = "elysium-mobile"
}

provider "aws" {
  region = "us-east-2"
}

# =====================================
# 1. Network Configuration
# =====================================
resource "aws_vpc" "app_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_hostnames = true
  tags                 = { Name = "ElysiumVPC" }
}

resource "aws_internet_gateway" "app_igw" {
  vpc_id = aws_vpc.app_vpc.id
}

# Subnet A - us-east-2a
resource "aws_subnet" "app_subnet" {
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-2a"
}

# Subnet B - us-east-2b (Requerida por el ALB)
resource "aws_subnet" "app_subnet_b" {
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = "10.0.2.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-2b"
}

resource "aws_route_table" "app_route_table" {
  vpc_id = aws_vpc.app_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.app_igw.id
  }
}

resource "aws_route_table_association" "app_route_assoc" {
  subnet_id      = aws_subnet.app_subnet.id
  route_table_id = aws_route_table.app_route_table.id
}

resource "aws_route_table_association" "app_route_assoc_b" {
  subnet_id      = aws_subnet.app_subnet_b.id
  route_table_id = aws_route_table.app_route_table.id
}

# =====================================
# 2. SSH Configuration
# =====================================
resource "aws_key_pair" "deployer" {
  key_name   = "elysium-key"
  public_key = file("${path.module}/.ssh/id_rsa_elysium.pub")
}

# =====================================
# 3. Security Groups
# =====================================

# SG del Load Balancer (Tráfico Público)
resource "aws_security_group" "lb_sg" {
  name        = "alb-sg"
  description = "Permitir HTTP y HTTPS publico hacia el ALB"
  vpc_id      = aws_vpc.app_vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# SG de la EC2 (Protegida detrás del ALB)
resource "aws_security_group" "app_sg" {
  name        = "app-sg"
  description = "Allow traffic from ALB and SSH"
  vpc_id      = aws_vpc.app_vpc.id

  # SSH habilitado para despliegues de GitHub Actions
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Solo acepta tráfico web si pasa a través del Load Balancer
  ingress {
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.lb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# =====================================
# 4. IAM Role para AWS SSM (Session Manager)
# =====================================
resource "aws_iam_role" "app_ssm_role" {
  name = "app-ssm-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = "sts:AssumeRole"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ssm_managed" {
  role       = aws_iam_role.app_ssm_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

resource "aws_iam_instance_profile" "app_ssm_profile" {
  name = "app-ssm-profile"
  path = "/"
  role = aws_iam_role.app_ssm_role.name
}

# =====================================
# 5. EC2 Instance
# =====================================
data "aws_ami" "ubuntu" {
  most_recent = true
  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
  owners = ["099720109477"]
}

resource "aws_instance" "app_server" {
  ami                    = data.aws_ami.ubuntu.id
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.app_subnet.id
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  key_name               = aws_key_pair.deployer.key_name
  iam_instance_profile   = aws_iam_instance_profile.app_ssm_profile.name

  root_block_device {
    volume_size           = 20
    volume_type           = "gp3"
    delete_on_termination = true
  }

  user_data = <<-EOF
              #!/bin/bash
              apt-get update -y
              curl -fsSL https://get.docker.com | sh

              curl -SL https://github.com/docker/compose/releases/download/v2.29.0/docker-compose-linux-x86_64 -o /usr/local/bin/docker-compose
              chmod +x /usr/local/bin/docker-compose

              systemctl start docker
              systemctl enable docker
              usermod -aG docker ubuntu

              mkdir -p /home/ubuntu/app
              chown -R ubuntu:ubuntu /home/ubuntu/app

              cat > /home/ubuntu/app/.env << 'ENVEOF'
              PORT=8080
              JWT_SECRET=un_secreto_seguro_de_al_menos_256_bits_para_firmar_tokens_12345
              JWT_EXPIRATION_DAYS=7
              ENVEOF

              chmod 644 /home/ubuntu/app/.env
              chown ubuntu:ubuntu /home/ubuntu/app/.env

              cat > /home/ubuntu/app/update-api-host.sh << 'SCRIPT'
              #!/bin/bash
              API_HOST=$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)
              echo "API_HOST=$API_HOST" >> /home/ubuntu/app/.env
              docker-compose up -d
              SCRIPT

              chmod +x /home/ubuntu/app/update-api-host.sh

              cat > /etc/systemd/system/spring-app.service << 'SERVICE'
              [Unit]
              Description=Spring Boot App with Docker Compose
              After=network.target

              [Service]
              Type=oneshot
              RemainAfterExit=yes
              ExecStart=/usr/bin/docker-compose up -d
              ExecStop=/usr/bin/docker-compose down
              Restart=always

              [Install]
              WantedBy=multi-user.target
              SERVICE

              systemctl enable spring-app
              EOF

  tags = { Name = "ElysiumAppServer-Prod" }
}

# =====================================
# 6. Application Load Balancer (ALB)
# =====================================
resource "aws_lb" "app_alb" {
  name               = "elysium-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lb_sg.id]
  subnets            = [aws_subnet.app_subnet.id, aws_subnet.app_subnet_b.id]
}

# Target Group apuntando al Nginx (Puerto 80) de la EC2
resource "aws_lb_target_group" "app_tg" {
  name        = "elysium-tg"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = aws_vpc.app_vpc.id
  target_type = "instance"

  health_check {
    path                = "/"
    port                = "80"
    protocol            = "HTTP"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

# Vinculación dinámica de la instancia con el Target Group
resource "aws_lb_target_group_attachment" "app_tg_attach" {
  target_group_arn = aws_lb_target_group.app_tg.arn
  target_id        = aws_instance.app_server.id
  port             = 80
}

# Listener HTTP que recibe las peticiones externas
resource "aws_lb_listener" "http_listener" {
  load_balancer_arn = aws_lb.app_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

# =====================================
# 7. Github Actions
# =====================================
resource "github_actions_secret" "update_ec2_host" {
  repository      = "backend"
  secret_name     = "EC2_HOST"
  plaintext_value = aws_instance.app_server.public_ip
}

# =====================================
# 8. Outputs
# =====================================
output "alb_dns_name" {
  value       = aws_lb.app_alb.dns_name
  description = "ESTE URL NUNCA CAMBIA. Registrar este valor como CNAME en DonDominio"
}

output "ec2_public_dns" {
  value       = aws_instance.app_server.public_dns
  description = "Copiar este valor y pégalo en la variable EC2_HOST de GitHub cada vez que recrees la infraestructura"
}

output "ssm_note" {
  value       = "Se puede acceder por Session Manager: AWS Console → EC2 → Instances → Select app_server → Session Manager → Open session"
  description = "Nota de acceso sin SSH"
}

output "ec2_public_ip" {
  value       = aws_instance.app_server.public_ip
  description = "IP publica numerica de la EC2"
}