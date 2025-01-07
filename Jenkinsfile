def appName = 'inventory-api'
def contextPath = '/inventory-api'

def git_credentials_id = scm.userRemoteConfigs[0].credentialsId
def git_repo = scm.userRemoteConfigs[0].url

pipeline {
    agent { label 'master' }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from ${git_repo}, branch ${params.BRANCH_NAME} using credentials ${git_credentials_id}"
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: ${git_repo}
                            credentialsId: ${git_credentials_id}
                        ]]
                    ])
                    sh 'ls -l'
                }
            }
        }
         stage('Environment') {
                env.JAVA_HOME="/opt/java/openjdk"
                env.M2_HOME="/usr/share/maven"
                env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

                sh """
                    java -version
                    mvn -version
                    echo project ${ocp_project}, route ${public_route_prefix}, base domain ${ocp_base_domain}
                """
         }
         stage('Prepare'){
                 appVersion = getFromPom('version')
                 sh """
                     echo app version ${appVersion}
                 """
        }
        stage('Build') {
            steps {
                script {
                    echo "Running build for ${params.BUILD_ENV} environment..."

                    switch (params.BRANCH_NAME.toLowerCase()) {
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
                            echo "Branch is not SIT, UAT, or STAGING. Defaulting to ${params.BUILD_ENV}"
                            break
                    }

                    if (params.ENABLE_TESTS) {
                        echo "Running tests..."
                        sh './mvnw test'
                    } else {
                        echo "Skipping tests as ENABLE_TESTS is set to false"
                    }
                }
            }
        }
    }
}
