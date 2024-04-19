resource "google_project" "gcp_project" {
  name            = local.project_id
  project_id      = local.project_id
  billing_account = var.billing_account
}
