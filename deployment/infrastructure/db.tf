resource "google_firestore_database" "database" {
  project = local.project_id
  name    = local.db_name
  location_id = var.region
  type        = "FIRESTORE_NATIVE"
}
