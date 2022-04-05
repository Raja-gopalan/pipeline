def call(Map params) {
 	
 	def chg_dir = "${params.chg_dir}"
 	def app_name = "{{params.app_name}"
 	
 	dir("${chg_dir}") {
 	withSonarQubeEnv('Sonarqube-samco') {
 	sh "/opt/sonar-scanner/bin/sonar-scanner  -Dsonar.projectName=${app_name} -Dsonar.projectKey=${app_name}"
 	}
 	}
 	dir("${chg_dir}") {
 		timeout(time: 1, unit: 'HOURS') {
        	    waitForQualityGate abortPipeline: true
 	}
 	}	
}
