pipeline {
    agent any
    parameters {
        string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'Branch to build')
        choice(name: 'BUILD_ENV', choices: ['SIT', 'UAT', 'PROD'], description: 'Environment for the build')
        booleanParam(name: 'ENABLE_TESTS', defaultValue: true, description: 'Run tests during the build')
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Building branch: ${params.BRANCH_NAME}"
                    echo "Build environment: ${params.BUILD_ENV}"
                    echo "Run tests: ${params.ENABLE_TESTS}"
                }
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/camilafaiza26/inventory-api',
                        credentialsId: '18c0ab3a-5347-4dfc-bdb5-27c689d83390'
                    ]]
                ])
            }
        }
        stage('Build') {
            steps {
                script {
                    echo "Running build for ${params.BUILD_ENV} environment..."
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
