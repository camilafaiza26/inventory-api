pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Building branch: ${params.BRANCH_NAME}"
                    echo "Build environment: ${params.BUILD_ENV}"
                    echo "Run tests: ${params.ENABLE_TESTS}"

                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: 'https://github.com/camilafaiza26/inventory-api',
                            credentialsId: '44cca29c-b0ba-450e-8b95-c6eccc0814a5'
                        ]]
                    ])
                }
            }
        }

        stage('Read POM') {
            steps {
                script {
                    try {
                        echo "Reading POM file..."

                        def pomFile = 'pom.xml'
                        if (!fileExists(pomFile)) {
                            error "POM file not found at: ${pomFile}"
                        }

                        // Parsing XML
                        def pomXml = new XmlParser().parse(pomFile)

                        // Ambil nilai dari tag dengan namespace Maven
                        def ns = [mvn: 'http://maven.apache.org/POM/4.0.0']
                        def projectName = pomXml['mvn:name']?.text()
                        def artifactId = pomXml['mvn:artifactId']?.text()
                        def version = pomXml['mvn:version']?.text()

                        // Cetak hasil
                        echo "Project Name: ${projectName ?: 'Unknown'}"
                        echo "Artifact ID: ${artifactId ?: 'Unknown'}"
                        echo "Version: ${version ?: 'Unknown'}"
                    } catch (Exception e) {
                        error "Failed to read POM file: ${e.message}"
                    }
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
