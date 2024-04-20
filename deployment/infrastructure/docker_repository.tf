resource "google_artifact_registry_repository" "docker_repository" {
  location      = var.region
  repository_id = "docker-images-${local.env_id}"
  format        = "DOCKER"
  count         = local.env_id == "ci" ? 1 : 0
}
