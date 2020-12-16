plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    target {
        targetJava("1.8")
    }
    sourceSets {
        val main by getting {
            dependencies {
                api(project(":rest-server-core"))
                api(asoft("result", vers.asoft.result))
                api(asoft("logging-core", vers.asoft.logging))
                api("io.ktor:ktor-server-cio:${vers.ktor}")
                api("io.ktor:ktor-network:${vers.ktor}")
                api(asoft("jwt-rs", vers.asoft.jwt))
            }
        }
    }
}