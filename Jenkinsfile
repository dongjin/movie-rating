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
 /*
         stage('Example Test') {
            agent { docker 'openjdk:8-jre' }
            steps {
                echo 'Hello, JDK'
                sh 'java -version'
                sh 'java -jar target/movie-rating-0.0.1-SNAPSHOT.jar'
            }
        }
*/
        stage ('Deployment') {
            steps {
                withMaven(maven : 'maven_3_5_3') {
 //                  sh 'mvn deploy'
                   sh 'mvn exec:java -DskipTests'
                }
            }
        }
    }
}