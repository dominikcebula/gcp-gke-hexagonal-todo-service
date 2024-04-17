resource "google_firestore_database" "database" {
  project     = var.project_id
  name        = var.db_name
  location_id = var.region
  type        = "FIRESTORE_NATIVE"
}
