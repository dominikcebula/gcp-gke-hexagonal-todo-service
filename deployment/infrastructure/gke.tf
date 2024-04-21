resource "google_container_cluster" "gke_cluster" {
  name             = "gke-cluster-${local.env_id}"
  location         = var.region
  enable_autopilot = true
}

data "google_client_config" "default" {}

provider "kubernetes" {
  host                   = "https://${google_container_cluster.gke_cluster.endpoint}"
  token                  = data.google_client_config.default.access_token
  cluster_ca_certificate = base64decode(google_container_cluster.gke_cluster.master_auth[0].cluster_ca_certificate)
}

module "todo-service-workload-identity" {
  source     = "terraform-google-modules/kubernetes-engine/google//modules/workload-identity"
  name       = "todo-service-gke-sa"
  namespace  = "default"
  project_id = local.project_id
  roles      = ["roles/firebase.admin"]
}
