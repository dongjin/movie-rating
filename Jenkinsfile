
pipeline {
    //agent any

 agent {
        docker {
            image 'maven:3-alpine'
            args '-v /root/.m2:/root/.m2'
        }
    }

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

        stage('Deploy to Dev') {
            steps {

                // sh 'java -jar target/movie-rating-0.0.1-SNAPSHOT.jar'
                echo 'write deployment script here'
            }
        }

        stage('Gate to QA') {
            steps {
                timeout(time: 7, unit: 'DAYS') {
                    milestone(1)
                    input message: 'Do you want to deploy to QA env ?', submitter: 'authenticated'
                }
            }
        }

        stage('Deploy to QA') {
            steps {

                echo 'write deployment script here'
            }
        }

         stage('Gate to UAT') {
             steps {
                 timeout(time: 7, unit: 'DAYS') {
                     milestone(2)
                     input message: 'Do you want to deploy to UAT env ?', submitter: 'authenticated'
                 }
             }
         }

         stage('Deploy to UAT') {
             steps {
                echo 'write deployment script here'
             }
         }
    }
}
