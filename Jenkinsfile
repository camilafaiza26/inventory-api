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
                    userRemoteConfigs: [[url: 'https://github.com/example/repo.git']]
                ])
            }
        }
        stage('Build') {
            steps {
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
