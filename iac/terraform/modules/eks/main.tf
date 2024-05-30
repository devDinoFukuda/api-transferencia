resource "aws_eks_cluster" "cluster-desafio-itau" {
  name     = "cluster-desafio-itau"
  role_arn = var.role_arn

  vpc_config {
    subnet_ids = var.subnet_ids
  }
}

resource "aws_launch_configuration" "eks_worker_lc" {
  name          = "eks-worker-lc"
  image_id      = "ami-0c55b159cbfafe1f0" # Altere para uma AMI válida
  instance_type = "t2.medium"
  iam_instance_profile = var.worker_role_profile_name

  lifecycle {
    create_before_destroy = true
  }
}


resource "aws_autoscaling_group" "eks_worker_asg" {
  desired_capacity     = 2
  max_size             = 2
  min_size             = 1
  vpc_zone_identifier  = var.subnet_ids
  launch_configuration = aws_launch_configuration.eks_worker_lc.id

  tag {
    key                 = "kubernetes.io/cluster/cluster-desafio-itau"
    value               = "owned"
    propagate_at_launch = true
  }
}