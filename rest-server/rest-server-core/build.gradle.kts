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
                api(asoft("persist-core", vers.asoft.persist))
                api(asoft("access-system", vers.asoft.access))
                api(asoft("result-core", vers.asoft.duality))
                api(asoft("logging-core", vers.asoft.logging))
                api(asoft("jwt-rs", vers.asoft.jwt))
            }
        }
    }
}

aSoftOSSLibrary(
    version = vers.asoft.rest,
    description = "A kotlin multiplatform library to help authoring servers"
)
