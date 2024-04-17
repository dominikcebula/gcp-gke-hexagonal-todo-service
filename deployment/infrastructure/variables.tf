variable "project_id" {
  type        = string
  description = "GCP Project ID that holds infrastructure components"
  nullable    = false
}

variable "region" {
  type        = string
  description = "GCP Region to hold infrastructure components"
  nullable    = false
}

variable "db_name" {
  type        = string
  description = "DB Name used by a service"
}
