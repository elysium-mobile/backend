terraform {
  required_version = "1.15.7"
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

variable "acm_certificate_arn" {
  type        = string
  description = "ARN del certificado SSL emitido por AWS Certificate Manager"
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

  # El mundo exterior accede al ALB por el puerto estándar 80
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

  # Solo acepta tráfico web directo al puerto de tu Spring Boot (8080) si viene del ALB
  ingress {
    from_port       = 8080
    to_port         = 8080
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
  instance_type          = "t3.small"
  subnet_id              = aws_subnet.app_subnet.id
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  key_name               = aws_key_pair.deployer.key_name
  iam_instance_profile   = aws_iam_instance_profile.app_ssm_profile.name

  root_block_device {
    volume_size           = 20
    volume_type           = "gp3"
    delete_on_termination = true
  }

  user_data = file("${path.module}/template/user_data.sh")

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

# Target Group apuntando directamente al puerto de Spring Boot (8080)
resource "aws_lb_target_group" "app_tg" {
  name        = "elysium-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.app_vpc.id
  target_type = "instance"

  health_check {
    path                = "/"    # Ruta raíz de tu API para verificar el estado de salud
    port                = "8080" # Corregido para que valide el puerto real
    protocol            = "HTTP"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}

# Vinculación de la instancia con el Target Group en el puerto correcto
resource "aws_lb_target_group_attachment" "app_tg_attach" {
  target_group_arn = aws_lb_target_group.app_tg.arn
  target_id        = aws_instance.app_server.id
  port             = 8080
}

# Listener HTTP externo que escucha las peticiones en el puerto estándar 80
resource "aws_lb_listener" "http_listener" {
  load_balancer_arn = aws_lb.app_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type = "redirect"

    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
    #
  }
}

# Listener HTTPS
resource "aws_lb_listener" "https_listener" {
  load_balancer_arn = aws_lb.app_alb.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"

  certificate_arn = var.acm_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.app_tg.arn
  }
}

# =====================================
# 7. Github Actions
# =====================================
resource "github_actions_secret" "update_ec2_host" {
  repository  = "backend"
  secret_name = "EC2_HOST"
  value       = aws_instance.app_server.public_ip
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