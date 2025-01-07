pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from ${params.REPO_URL}, branch ${params.BRANCH_NAME} using credentials ${params.CREDENTIALS_ID}"
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
                        sh './mvnw test' // Example test command; adjust as needed
                    } else {

