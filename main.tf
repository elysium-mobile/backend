terraform {
  required_version = "1.15.5"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
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

resource "aws_subnet" "app_subnet" {
  vpc_id                  = aws_vpc.app_vpc.id
  cidr_block              = "10.0.1.0/24"
  map_public_ip_on_launch = true
  availability_zone       = "us-east-2a"
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

# =====================================
# 2. SSH Configuration
# =====================================
resource "aws_key_pair" "deployer" {
  key_name   = "elysium-key"
  public_key = file("${path.module}/.ssh/id_rsa_elysium.pub")
}

# =====================================
# 3. Security Group
# =====================================
resource "aws_security_group" "app_sg" {
  name        = "app-sg"
  description = "Allow traffic HTTP 8080 and SSH 22 - Solo IP Peru"
  vpc_id      = aws_vpc.app_vpc.id

  # SSH
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # API
  ingress {
    from_port   = 8080
    to_port     = 8080
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
# 5. EC2 Instance (con Elastic IP + SSM)
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
              apt-get install -y docker.io docker-compose
              systemctl start docker
              systemctl enable docker
              usermod -aG docker ubuntu

              mkdir -p /home/ubuntu/app

              cat > /home/ubuntu/app/.env << 'ENVEOF'
              PORT=8080
              JWT_SECRET=un_secreto_seguro_de_al_menos_256_bits_para_firmar_tokens_12345
              JWT_EXPIRATION_DAYS=7
              ENVEOF

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
# 6. Elastic IP
# =====================================
resource "aws_eip" "app_eip" {
  domain   = "vpc"
  instance = aws_instance.app_server.id
}

# =====================================
# 7. Outputs
# =====================================
output "public_ip" {
  value       = aws_eip.app_eip.public_ip
  description = "Elastic IP fija de tu EC2 (no cambia)"
}

output "public_dns" {
  value       = aws_eip.app_eip.public_dns
  description = "DNS público de tu EC2"
}

output "api_url" {
  value       = "http://${aws_eip.app_eip.public_ip}:8080"
  description = "URL completa de tu API Spring Boot"
}

output "ssm_note" {
  value       = "Puedes acceder por Session Manager: AWS Console → EC2 → Instances → Select app_server → Session Manager → Open session"
  description = "Nota de acceso sin SSH"
}