job("Build & Test Project") {
    startOn {
        codeReviewOpened {
            enabled = true
        }
    }
    gradlew("markhobson/maven-chrome:jdk-11", "build")
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