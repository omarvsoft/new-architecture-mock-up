pipeline {
  agent any
  stages {
    stage('Compilation') {
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
        }
        
      }
      steps {
        sh 'mvn -B -DskipTests clean package'
      }
    }
  }
  environment {
    test = 'tes'
  }
}