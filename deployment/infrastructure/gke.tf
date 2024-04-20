resource "google_container_cluster" "gke_cluster" {
  name             = "gke-cluster-${local.env_id}"
  location         = var.region
  enable_autopilot = true
}
