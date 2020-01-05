#!groovy
pipeline {
    agent any

    tools {
        maven "apache-maven-3.6.1"
        jdk 'jdk-11.0.1'
    }

    stages {
        stage('获取代码'){
            steps {
                // 清空当前目录
                deleteDir()
                // 拉取代码
                git branch: 'master', credentialsId: '264007b5-a8fa-4200-9734-c246ead10564', url: 'http://47.75.52.43/lottery/lottery-gateway.git'
            }
        }

        stage('编译代码') {
            steps {
                sh 'mvn -T 1C clean package -Dmaven.test.skip=true'
            }
        }

        stage('构建镜像') {
            steps {
                configFileProvider([configFile(fileId: '3b07e6f0-00b7-4623-a829-48e899b528b1', variable: 'MAVEN_GLOBAL_SETTINGS')]) {
                    sh 'mvn -gs $MAVEN_GLOBAL_SETTINGS -T 1C jib:build'
                }
            }
        }
    }
}