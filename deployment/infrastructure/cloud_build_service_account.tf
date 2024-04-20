resource "google_project_iam_member" "cloud_build_service_account_iam_policy" {
  project = local.project_id
  role    = "roles/firebase.admin"
  member  = "serviceAccount:398247215074@cloudbuild.gserviceaccount.com"
}
