pipeline {
    agent any

    // Outils à définir dans la configuration Jenkins (Manage Jenkins -> Global Tool Configuration)
    tools {
        maven 'Maven' // Correspond au nom configuré pour Maven dans Jenkins
        jdk 'JDK 17'  // Correspond au nom configuré pour le JDK 17 dans Jenkins
    }

    environment {
        // Variables d'environnement
        SONAR_HOST_URL = 'http://host.docker.internal:9000'
    }

    stages {
        stage('Checkout') {
            steps {
                // Récupération du code source depuis Git
                checkout scm
            }
        }

stage('Build') {
            steps {
                echo 'Compilation du projet...'
                sh 'mvn clean compile'
            }
        }

        stage('Test & Code Coverage') {
            steps {
                echo 'Exécution des tests et génération du rapport JaCoCo...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = credentials('sonar-token')
            }
            steps {
                echo 'Analyse de la qualité du code avec SonarQube...'
                sh 'mvn sonar:sonar -Dsonar.projectKey=bad-practices-app -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_TOKEN}'
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Package & Docker Build') {
            steps {
                echo 'Création du JAR exécutable et de l\'image Docker...'
                
                // TODO : Ajouter la commande Maven pour créer le package complet (JAR) en ignorant les tests
                // sh '...'
                
                // TODO : Ajouter la commande Docker pour construire l'image avec le tag "epsi/bad-practices-app:latest"
                // sh '...'
            }
        }
    }
}
