pipeline {
    agent any

    tools{
        maven "apache-maven-3.3.9"
        jdk 'jdk-11.0.4'
    }

    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}