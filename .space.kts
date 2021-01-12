job("Build & Test Project") {
    container("markhobson/maven-chrome:jdk-11") {
        kotlinScript { api ->
            if (api.gitBranch() == "refs/heads/master-dev") {
                api.gradlew("build")
            } else {
                println("Code submitted")
            }
        }
    }
}

job("Deploy Project") {
    startOn {
        gitPush {
            enabled = true
            branchFilter {
                +"refs/heads/master"
            }
        }
    }
    gradlew("markhobson/maven-chrome:jdk-11", "publish")
}