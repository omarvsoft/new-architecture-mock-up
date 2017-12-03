pipeline {
  agent any
  stages {
    stage('Compilation') {
      steps {
        sh 'mvn clean install'
      }
    }
  }
  environment {
    test = 'tes'
  }
}