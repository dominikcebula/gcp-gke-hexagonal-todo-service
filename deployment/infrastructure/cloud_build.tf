data "google_secret_manager_secret" "github-token-secret" {
  secret_id = "github-token-secret"
}

data "google_secret_manager_secret_version" "github-token-secret-version" {
  secret = data.google_secret_manager_secret.github-token-secret.id
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
    app_installation_id = 123123
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
