plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
    id("io.codearte.nexus-staging")
    signing
}

kotlin {
    multiplatformLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(asoft("files", vers.asoft.files))
                api(asoft("form-http", vers.asoft.form))
                api(asoft("persist-core", vers.asoft.persist))
                api(asoft("result-core", vers.asoft.duality))
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.rest,
    description = "A kotlin multiplatform library to handle rest requests with ktor-client"
)
