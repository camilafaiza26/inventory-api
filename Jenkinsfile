pipeline {
    agent { label 'master' }

    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'main', description: 'Branch to checkout')
        string(name: 'BUILD_ENV', defaultValue: 'dev', description: 'Build environment (e.g., dev, sit, uat, prod)')
        booleanParam(name: 'ENABLE_TESTS', defaultValue: true, description: 'Run tests after build?')
    }

    environment {
        JAVA_HOME = "/opt/java/openjdk"
        M2_HOME = "/usr/share/maven"
        PATH = "${env.JAVA_HOME}/bin:${env.M2_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    def git_repo = scm.userRemoteConfigs[0].url
                    def git_credentials_id = scm.userRemoteConfigs[0].credentialsId

                    echo "Checking out code from ${git_repo}, branch ${params.BRANCH_NAME} using credentials ${git_credentials_id}"
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: git_repo,
                            credentialsId: git_credentials_id
                        ]]
                    ])
                    sh 'ls -l'
                }
            }
        }

        stage('Environment') {
            steps {
                script {
                    sh """
                        echo 'Java and Maven Environment Details:'
                        java -version
                        mvn -version
                    """
                }
            }
        }

        stage('Prepare') {
            steps {
                script {
                    def appVersion = getFromPom('version')
                    echo "App Version: ${appVersion}"
                }
            }
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

def getFromPom(String key) {
    def pom = readMavenPom file: 'pom.xml'
    if (pom."${key}") {
        return pom."${key}"
    } else {
        error "Key '${key}' not found in POM file"
    }
}