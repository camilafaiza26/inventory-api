def git_credentials_id = scm.userRemoteConfigs[0].credentialsId
def git_repo = scm.userRemoteConfigs[0].url
def git_branch = scm.branches[0].name

pipeline {
    agent any
    stages {
       stage('Checkout') {
          dir('project') {
              git url: "${git_repo}", branch: "${git_branch}", credentialsId: "${git_credentials_id}"
          }
          sh 'ls -l'
       }
        stage('Read POM') {
            steps {
                script {
                    echo "Reading POM file..."
                    def pom = readMavenPom file: 'pom.xml'

                    // Extract and print project details
                    echo "Project Name: ${pom.name ?: 'Unknown'}"
                    echo "Artifact ID: ${pom.artifactId ?: 'Unknown'}"
                    echo "Version: ${pom.version ?: 'Unknown'}"
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Running build for ${params.BUILD_ENV} environment..."

                    switch (params.BRANCH_NAME) {
                        case 'sit':
                            echo "Branch is SIT, using environment SIT"
                            break
                        case 'uat':
                            echo "Branch is UAT, using environment UAT"
                            break
                        case 'staging':
                            echo "Branch is STAGING, using environment STAGING"
                            break
                        default:
                            echo "Branch is not SIT, UAT, or STAGING. Defaulting to ${params.BRANCH_NAME}"
                            break
                    }

                    if (params.ENABLE_TESTS) {
                        echo "Running tests..."
                    } else {
                        echo "Skipping tests..."
                    }
                }
            }
        }
    }
}
