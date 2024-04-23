resource "google_project_iam_member" "cloud_build_service_account_iam_policy_firebase" {
  project = local.project_id
  role    = "roles/firebase.admin"
  member  = "serviceAccount:${data.google_project.project.number}@cloudbuild.gserviceaccount.com"
  count   = local.env_id == "ci" ? 1 : 0
}

resource "google_project_iam_member" "cloud_build_service_account_iam_policy_cloud_deploy" {
  project = local.project_id
  role    = "roles/clouddeploy.releaser"
  member  = "serviceAccount:${data.google_project.project.number}@cloudbuild.gserviceaccount.com"
  count   = local.env_id == "ci" ? 1 : 0
}

resource "google_project_iam_member" "cloud_build_service_account_iam_policy_service_account_user" {
  project = local.project_id
  role    = "roles/iam.serviceAccountUser"
  member  = "serviceAccount:${data.google_project.project.number}@cloudbuild.gserviceaccount.com"
  count   = local.env_id == "ci" ? 1 : 0
}

data "google_secret_manager_secret" "github-token-secret" {
  secret_id = "todo-service-github-connection-github-oauthtoken"
}

data "google_secret_manager_secret_version" "github-token-secret-version" {
  secret = data.google_secret_manager_secret.github-token-secret.id
}

data "google_secret_manager_secret" "github-app-installation-id" {
  secret_id = "todo-service-github-connection-app-installation-id"
}

data "google_secret_manager_secret_version" "github-app-installation-id-version" {
  secret = data.google_secret_manager_secret.github-app-installation-id.id
}

data "google_iam_policy" "p4sa-secretAccessor" {
  binding {
    role    = "roles/secretmanager.secretAccessor"
    members = ["serviceAccount:service-${data.google_project.project.number}@gcp-sa-cloudbuild.iam.gserviceaccount.com"]
  }
}

resource "google_secret_manager_secret_iam_policy" "policy" {
  secret_id   = data.google_secret_manager_secret.github-token-secret.secret_id
  policy_data = data.google_iam_policy.p4sa-secretAccessor.policy_data
}

resource "google_cloudbuildv2_connection" "todo-service-github-connection" {
  location = var.region
  name     = "todo-service-github-connection"

  github_config {
    app_installation_id = tonumber(data.google_secret_manager_secret_version.github-app-installation-id-version.secret_data)
    authorizer_credential {
      oauth_token_secret_version = data.google_secret_manager_secret_version.github-token-secret-version.id
    }
  }
}

resource "google_cloudbuildv2_repository" "todo-service-github-repo" {
  location          = var.region
  name              = "todo-service-github-repo"
  parent_connection = google_cloudbuildv2_connection.todo-service-github-connection.name
  remote_uri        = "https://github.com/dominikcebula/gcp-gke-hexagonal-todo-service.git"
}
