node('master') {
	stage('Prepare') {
		checkout scm
		sh './gradlew setupCiWorkspace clean --refresh-dependencies'
	}
	stage('Build') {
		sh './gradlew build'
		archive 'build/libs/*jar'
	}
}