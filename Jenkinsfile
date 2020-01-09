pipeline {
    agent any

    tools {
        maven "apache-maven-3.3.9"
        jdk 'jdk1.8.0_181'
    }

    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}