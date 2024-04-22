resource "google_clouddeploy_target" "delivery_pipeline_target" {
  location    = var.region
  name        = "gke-cluster-ci-target"
  description = "todo service gke deployment target"

  gke {
    cluster = "projects/${local.project_id}/locations/${var.region}/clusters/${google_container_cluster.gke_cluster.name}"
  }

  project          = local.project_id
  require_approval = false
}

resource "google_clouddeploy_delivery_pipeline" "delivery_pipeline" {
  location    = var.region
  name        = "${local.project_id}-pipeline"
  description = "todo service pipeline"
  project     = local.project_id

  serial_pipeline {
    stages {
      target_id = google_clouddeploy_target.delivery_pipeline_target.name
    }
  }
}
