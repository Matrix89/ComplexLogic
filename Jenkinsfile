pipeline {
	agent any
	stages {
		stage('Prepare') {
			checkout scm
			sh './gradlew setupCiWorkspace clean --refresh-dependencies'
		}
		stage('Build') {
			steps {
				sh './gradlew build'
				archive 'build/libs/*jar'
			}
		}
	}
}