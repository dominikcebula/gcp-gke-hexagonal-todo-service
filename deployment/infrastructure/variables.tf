variable "region" {
  type        = string
  description = "GCP Region to hold infrastructure components"
  default = "europe-central2"
  nullable    = false
}

variable "billing_account" {
  type        = string
  description = "Billing account used for project and services within a project"
  nullable    = false
}
