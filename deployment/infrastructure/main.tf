terraform {
  required_providers {
    google-beta = {
      source  = "hashicorp/google-beta"
      version = "~> 4.0"
    }
  }
}

locals {
  project_id = "todo-service-${var.env_id}"
  db_name    = "todo-service-${var.env_id}-db"
}

provider "google" {
  project = local.project_id
  region  = var.region
}
