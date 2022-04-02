def call() {
	echo "build started"
            
            def last_commit_ID = readFile '/home/samco/var/log/hrms-samco-master-last-deploy-id.txt'
            dir ('/home/samco/Libraries/hrms-samco') {
                git branch: 'dev', credentialsId: '6df82309-aa2f-4274-81d7-956a5c84a3a0', url: 'https://bitbucket.org/samco-team/hrms.git'
                echo "${last_commit_ID}"
            
            def affected_files = sh (script: "git diff --name-only HEAD ${last_commit_ID}", returnStdout: true)
            
            echo '-------------------'
            echo "${affected_files}"
            echo '-------------------'
            
            writeFile file: "/home/samco/var/log/affected_files.txt",
            text: affected_files
            
            
            def latest_commit = sh (script: "git log --format='%H' -n 1", returnStdout: true)
            def author_name = sh (script: "git log --format='%an %ce' -n 1", returnStdout: true)
            def date_of_build = sh (script: "date", returnStdout: true)
            echo "Last commit id is"
            echo "last_commit_ID"
            echo "Latest commit is is"
            echo "latest_commit"
            //def diff_commits_ids = sh (script: "git rev-list --ancestry-path ${last_commit_ID}..${latest_commit}")
            
            echo "${latest_commit}"
            
            echo "=========================="
            echo "These are the diff commits between last & latest commits"
            echo "${diff_commits_ids}"
            echo "=========================="
            
            echo "https://bitbucket.org/samco-team/hrms/commits/${latest_commit}"
            
            def latest_commit_URL = "https://bitbucket.org/samco-team/hrms/commits/${latest_commit}"
            
            
            echo '=================='
            echo latest_commit_URL
            echo '=================='
            
            
           emailext attachmentsPattern: "affected_files.txt", body:"<h3 style=text-align: left;><img src=/home/rajagopalan/Downloads/jenkins2.png alt='' width=75 height=100 />Build Approval</h3><table border=1><tbody><tr><td>Project Name:</td><td>${JOB_NAME}</td></tr><tr><td>Environment:</td><td>&nbsp;</td></tr><tr><td>Committer Name:</td><td>${author_name}</td></tr><tr><td>Build URL:</td><td>${BUILD_URL}</td></tr><tr><td>Date of Build:</td><td>${date_of_build}</td></tr></tbody></table><p>&nbsp;</p><p>Please go to console and approve</p><p><a href=${BUILD_URL}input>${BUILD_URL}</a></p><h3>Changes</h3><p><a href=${latest_commit_URL}input>${latest_commit_URL} </a></p><p>&nbsp;</p><p>Regards,<strong>DevOps Team - Samco Securities</strong><strong><a href=mailto:devops@samco.in>devops@samco.in</a></strong> </br>Please be informed that this is an automated email generated from system. For any queries, kindly reach out to devops@samco.in.</p>",
            mimeType: 'text/html',
            subject: "[Jenkins] ${JOB_NAME} Build approval request",
            to: "${rajagopalan}"
            recipientProviders: [[$class: 'CulpritsRecipientProvider']]
            userInput = input submitter: 'jenkins', message: "Do you approve ?"
            }    
}
