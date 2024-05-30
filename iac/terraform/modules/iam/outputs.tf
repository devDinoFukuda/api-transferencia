output "eks_role_arn" {
  description = "ARN EKS Role"
  value = aws_iam_role.eks_role.arn
}

output "worker_role_profile_name" {
  description = "IAM Instance Profile for Worker Roles"
  value = aws_iam_instance_profile.eks_worker_profile.name
}