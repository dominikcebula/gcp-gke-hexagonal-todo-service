terraform {
  required_providers {
    google-beta = {
      source  = "hashicorp/google-beta"
      version = "~> 4.0"
    }
  }
}

locals {
  env_id     = terraform.workspace
  project_id = "todo-service-${local.env_id}"
  db_name    = "todo-service-${local.env_id}-db"
}

provider "google" {
  project = local.project_id
  region  = var.region
}

data "google_project" "project" {
  project_id = local.project_id
}
