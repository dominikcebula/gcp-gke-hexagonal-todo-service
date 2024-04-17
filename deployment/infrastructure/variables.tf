variable "project_id" {
  type     = string
  default  = "GCP Project ID that holds infrastructure components"
  nullable = false
}

variable "region" {
  type     = string
  default  = "GCP Region to hold infrastructure components"
  nullable = false
}
