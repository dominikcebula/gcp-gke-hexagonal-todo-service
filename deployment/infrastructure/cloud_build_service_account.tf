resource "google_project_iam_member" "cloud_build_service_account_iam_policy" {
  project = local.project_id
  role    = "roles/firebase.admin"
  member = "serviceAccount:${data.google_project.project.number}@cloudbuild.gserviceaccount.com"
  count = local.env_id == "ci" ? 1 : 0
}
