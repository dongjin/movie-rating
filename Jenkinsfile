
pipeline {
    agent any

    stages {
        stage ('Compile') {
            steps {
                withMaven(maven : 'maven_3_5_3') {
                    sh 'mvn clean compile'
                }
            }
        }

        stage ('Test') {
            steps {
                withMaven(maven : 'maven_3_5_3') {
                    sh 'mvn test'
                }
            }
        }

        stage('Deliver') {
            steps {
//                sh './deliver.sh'

sh 'mvn jar:jar install:install help:evaluate -Dexpression=project.name'
sh 'NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`'
sh 'VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`'
sh 'java -jar target/${NAME}-${VERSION}.jar'
            }
        }
    }
}
