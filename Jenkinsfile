pipeline {
    agent any
    
    stages {
        // Stage 1: Checkout code from GitHub
        stage('Checkout') {
            steps {
                script {
                    // Check if this is a PR (GitHub/Bitbucket)
                    if (env.CHANGE_ID) {
                        echo "Building PR #${env.CHANGE_ID} from branch: ${env.CHANGE_BRANCH}"
                        git branch: env.CHANGE_BRANCH, // PR's source branch
                             url: 'https://github.com/alex19rosario/ROS_LMS_Backend.git'
                    }
                    // Default to 'development' for non-PR triggers
                    else {
                        echo "Building default branch: development"
                        git branch: 'development',
                             url: 'https://github.com/alex19rosario/ROS_LMS_Backend.git'
                    }
                }
            }
        }

        // Stage 2: Run tests, check test coverage and build
        stage('Test and Build') {
            steps {
                echo 'Testing and building Spring Boot application with Maven Wrapper...'
                // Set execute permissions for mvnw
                sh 'chmod +x mvnw'
                // Run the build
                sh './mvnw clean verify' //

                // Archive the JaCoCo exec files
                archiveArtifacts '**/target/jacoco.exec'

                // Publish HTML report (requires HTML Publisher plugin)
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'coverage-report/target/site/jacoco-aggregate',
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage Report'
                ])

                // Simple coverage recording (optional)
                recordCoverage(
                    tools: [[parser: 'JACOCO']],
                    id: 'jacoco',
                    name: 'JaCoCo Coverage'
                )
            }
        }
        // Stage 3: SonarQube Static Code Analysis
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube-server') {
                    sh './mvnw sonar:sonar'
                }
            }
        }

    }
    
    post {
        success {
            echo '✅ Pipeline completed successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}
