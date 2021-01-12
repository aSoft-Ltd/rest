plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

group = "tz.co.asoft"
version = vers.asoft.rest

kotlin {
    multiplatformLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(asoft("files", vers.asoft.files))
                api(asoft("form-http", vers.asoft.form))
                api(asoft("persist-core", vers.asoft.persist))
                api(asoft("result", vers.asoft.result))
            }
        }
    }
}

configurePublishing {
    repositories {
        maven("https://maven.pkg.jetbrains.space/asofttz/p/libs/maven") {
            name = "space"
            credentials {
                username = System.getenv("SPACE_USERNAME") ?: System.getenv("JB_SPACE_CLIENT_ID")
                password = System.getenv("SPACE_PASSWORD") ?: System.getenv("JB_SPACE_CLIENT_SECRET")
            }
        }
    }
}