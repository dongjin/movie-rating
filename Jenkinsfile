
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

  stage('Gate to deliver') {
            steps {
                timeout(time: 7, unit: 'DAYS') {
                    milestone(10)
                    input message: 'Do you want to deploy to qat ?', submitter: 'authenticated'
                }
            }
        }



        stage('Deliver') {
            steps {
                sh 'java -jar target/movie-rating-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}
