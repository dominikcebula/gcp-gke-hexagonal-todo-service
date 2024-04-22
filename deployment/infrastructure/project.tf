resource "google_project" "gcp_project" {
  name            = local.project_id
  project_id      = local.project_id
  billing_account = var.billing_account
}

resource "google_project_service" "service_container" {
  project = local.project_id
  service = "container.googleapis.com"
}

resource "google_project_service" "service_containerfilesystem" {
  project = local.project_id
  service = "containerfilesystem.googleapis.com"
}

resource "google_project_service" "service_containerregistry" {
  project = local.project_id
  service = "containerregistry.googleapis.com"
}

resource "google_project_service" "service_cloudbuild" {
  project = local.project_id
  service = "cloudbuild.googleapis.com"
}

resource "google_project_service" "service_clouddeploy" {
  project = local.project_id
  service = "clouddeploy.googleapis.com"
}

resource "google_project_service" "service_artifactregistry" {
  project = local.project_id
  service = "artifactregistry.googleapis.com"
}

resource "google_project_service" "service_autoscaling" {
  project = local.project_id
  service = "autoscaling.googleapis.com"
}

resource "google_project_service" "service_cloudresourcemanager" {
  project = local.project_id
  service = "cloudresourcemanager.googleapis.com"
}

resource "google_project_service" "service_compute" {
  project = local.project_id
  service = "compute.googleapis.com"
}

resource "google_project_service" "service_dns" {
  project = local.project_id
  service = "dns.googleapis.com"
}

resource "google_project_service" "service_firebaserules" {
  project = local.project_id
  service = "firebaserules.googleapis.com"
}

resource "google_project_service" "service_firestore" {
  project = local.project_id
  service = "firestore.googleapis.com"
}

resource "google_project_service" "service_gkebackup" {
  project = local.project_id
  service = "gkebackup.googleapis.com"
}

resource "google_project_service" "service_iam" {
  project = local.project_id
  service = "iam.googleapis.com"
}

resource "google_project_service" "service_iamcredentials" {
  project = local.project_id
  service = "iamcredentials.googleapis.com"
}

resource "google_project_service" "service_logging" {
  project = local.project_id
  service = "logging.googleapis.com"
}

resource "google_project_service" "service_monitoring" {
  project = local.project_id
  service = "monitoring.googleapis.com"
}

resource "google_project_service" "service_networkconnectivity" {
  project = local.project_id
  service = "networkconnectivity.googleapis.com"
}

resource "google_project_service" "service_secretmanager" {
  project = local.project_id
  service = "secretmanager.googleapis.com"
}

resource "google_project_service" "service_storage_api" {
  project = local.project_id
  service = "storage-api.googleapis.com"
}

resource "google_project_service" "service_storage" {
  project = local.project_id
  service = "storage.googleapis.com"
}

resource "google_project_service" "service_sts" {
  project = local.project_id
  service = "sts.googleapis.com"
}

resource "google_project_service" "service_analyticshub" {
  project = local.project_id
  service = "analyticshub.googleapis.com"
}
