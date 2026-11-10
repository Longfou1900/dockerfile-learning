pipeline {
    agent {any
        docker {
            image 'node:alpine'
            agrs '-u root'
        }
    }

    stages {
        stage('Git Checkout'){
            steps{
                git 'https://github.com/Longfou1900/autoluxe-car-v3.git'
            }
        }


        stage('run'){
            steps{
                sh '''
                node --version
                npm --version

                npm install --force
                npm run test
                '''
            }
        }
        
    }
}
