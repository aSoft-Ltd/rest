plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
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
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.rest,
    description = "A kotlin multiplatform library to help authoring servers with ktor"
)
