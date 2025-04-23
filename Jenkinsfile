pipeline {
    agent any
    
    stages {
        // Stage 1: Checkout code from GitHub
        stage('Checkout') {
            steps {
                git branch: 'development',
                url: 'https://github.com/alex19rosario/ROS_LMS_Backend.git'
            }
        }

        // Stage 2: Run tests, check test coverage and build
        stage('Test and Build') {
            steps {
                echo 'Testing and building Spring Boot application with Maven Wrapper...'
                // Set execute permissions for mvnw
                sh 'chmod +x mvnw'
                // Run the build
                sh './mvnw clean verify' // Fails build if thresholds (80%) not met

                recordCoverage(
                    tools: [[parser: 'JACOCO']],
                    id: 'jacoco',
                    name: 'JaCoCo Coverage',
                    sourceFileResolver: [$class: 'JacocoSourceFileResolver'],
                    sourceCodeRetention: 'EVERY_BUILD',
                    sourceDirectories: [
                        'adapters/src/main/java',
                        'application/src/main/java',
                        'domain/src/main/java',
                        'infrastructure/src/main/java'
                    ],
                    classDirectories: [
                        '**/target/classes',
                        '**/target/test-classes'
                    ],
                    qualityGates: [
                        [threshold: 80.0, metric: 'LINE', baseline: 'PROJECT', unstable: true],
                        [threshold: 80.0, metric: 'METHOD', baseline: 'PROJECT', unstable: true],
                    ]
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
