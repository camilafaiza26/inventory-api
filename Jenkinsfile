pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from ${scm.userRemoteConfigs[0].url}, branch ${params.BRANCH_NAME} using credentials ${scm.userRemoteConfigs[0].credentialsId}"
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: scm.userRemoteConfigs[0].url,
                            credentialsId: scm.userRemoteConfigs[0].credentialsId
                        ]]
                    ])
                    sh 'ls -l'
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
                        sh './mvnw test' // Adjust as needed for your test command
                    } else {
                        echo "Skipping tests as ENABLE_TESTS is set to false"
                    }
                }
            }
        }
    }
}
