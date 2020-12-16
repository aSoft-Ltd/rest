plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
}

group = "tz.co.asoft.rest"
version = vers.asoft.rest

kotlin {
    jvm {
        targetJava("1.8")
    }
    js(IR) {
        browser {}
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":rest-client-ktor"))
                api(asoft("viewmodel-core",vers.asoft.viewmodel))
            }
        }
    }
}

