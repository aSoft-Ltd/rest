plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

group = "tz.co.asoft"
version = vers.asoft.rest

kotlin {
    target {
        targetJava("1.8")
    }
    sourceSets {
        val main by getting {
            dependencies {
                api("io.ktor:ktor-server-cio:${vers.ktor}")
                api("io.ktor:ktor-network:${vers.ktor}")
                api(project(":rest-server-core"))
                api(asoft("access-system", vers.asoft.access))
                api(asoft("result-core", vers.asoft.duality))
                api(asoft("logging-core", vers.asoft.logging))
                api(asoft("jwt-rs", vers.asoft.jwt))
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