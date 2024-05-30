resource "aws_dynamodb_table" "Transacao" {
  name           = "Transacao"
  billing_mode   = "PAY_PER_REQUEST"
  hash_key       = "idTransacao"

  attribute {
    name = "idTransacao"
    type = "S"
  }
}