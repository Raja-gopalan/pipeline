//import sonar-scan.groovy


def call(Map params) {
  def last_deploy_id_file = params.last_deploy_id_file
  def chg_dir = "${params.chg_dir}"
  def branch = "${params.branch}"
  def cred_ID = "${params.cred_ID}"
  def repo_URL = "${params.repo_URL}"
  def commit_URL = "${params.commit_URL}"
  def mailTO = "${params.mailTO}"
  def CCmailTO = "${params.CCmailTO}"
  echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<'
  echo last_deploy_id_file
  echo chg_dir
  echo branch
  echo cred_ID
  echo repo_URL
  echo commit_URL
  echo mailTO
  echo CCmailTO
  echo '>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
    
  def last_commit_ID = readFile "${last_deploy_id_file}"
  dir ("${chg_dir}"){
    git branch: "${branch}", credentialsId: "${cred_ID}", url: "${repo_URL}"
    
    def latest_commit = sh (script: "git log --format='%H' -n 1", returnStdout: true)
    def author_name = sh (script: "git log --format='%an %ce' -n 1", returnStdout: true)
    def date_of_build = sh (script: "date", returnStdout: true)git rev-list --ancestry-path 
    def diff_commits = sh (script: "git rev-list --ancestry-path 
    
    echo '================='
    echo 'Last Commit ID is'
    echo "${last_commit_ID}"
    echo '================='
    echo '================='
    echo 'Latest commit id is:'
    echo "latest_commit"
    echo '================='
    
    def latest_commit_URL = "${commit_URL}/${latest_commit}"
    
    echo '=================='
    echo latest_commit_URL
    echo '=================='
    
    echo '=================='
    echo 'starting sonar-scanner'
    echo '=================='
                        
    evaluate(new File("sonar-scan.groovy"))                           
    sonar(chg_dir: '/opt/jenkins/workspace/RankMF-Stag', app_name: '${app_name}')
                           
                           
    //Email functions started here
    emailext body:"<h3 style=text-align: left;><img src=/home/rajagopalan/Downloads/jenkins2.png alt='' width=75 height=100 />Build Approval</h3><table border=1><tbody><tr><td>Project Name:</td><td>${JOB_NAME}</td></tr><tr><td>Environment:</td><td>&nbsp;</td></tr><tr><td>Committer Name:</td><td>${author_name}</td></tr><tr><td>Build URL:</td><td>${BUILD_URL}</td></tr><tr><td>Date of Build:</td><td>${date_of_build}</td></tr></tbody></table><p>&nbsp;</p><p>Please go to console and approve</p><p><a href=${BUILD_URL}input>${BUILD_URL}</a></p><h3>Changes</h3><p><a href=${latest_commit_URL}input>${latest_commit_URL} </a></p><p>&nbsp;</p><p>Regards,<strong>DevOps Team - Samco Securities</strong><strong><a href=mailto:devops@samco.in>devops@samco.in</a></strong> </br>Please be informed that this is an automated email generated from system. For any queries, kindly reach out to devops@samco.in.</p>",
      mimeType: 'text/html',
      subject: "[Jenkins] ${JOB_NAME} Build approval request",
      to: "${mailTO}, ${CCmailTO}"
      recipientProviders: [[$class: 'CulpritsRecipientProvider']]
      userInput = input submitter: 'jenkins', message: "Do you approve ?"
    
  }
  
  
}
