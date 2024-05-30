provider "aws" {
  region                      = "us-east-1"
  access_key                  = "test"
  secret_key                  = "test"
  skip_credentials_validation = true
  skip_requesting_account_id  = true
  endpoints {
    eks                            = "http://localhost:4566"
    iam                           = "http://localhost:4566"
    ec2                           = "http://localhost:4566"
    dynamodb               = "http://localhost:4566"
    sqs                            = "http://localhost:4566"
  }
}

module "dynamodb" {
  source = "./modules/dynamodb"
}

module "sqs" {
  source = "./modules/sqs"
}

module "iam" {
  source = "./modules/iam"
}

module "eks" {
  source = "./modules/eks"
  role_arn = module.iam.eks_role_arn
  worker_role_profile_name = module.iam.worker_role_profile_name
  subnet_ids = ["subnet-12345678", "subnet-87654321"]
}