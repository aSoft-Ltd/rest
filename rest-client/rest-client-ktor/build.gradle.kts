plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("tz.co.asoft.library")
}

kotlin {
    universalLib()
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(asoft("files", vers.asoft.files))
                api(asoft("form-http", vers.asoft.form))
                api(asoft("persist-core", vers.asoft.persist))
                api(asoft("paging-core", vers.asoft.persist))
                api(asoft("result", vers.asoft.result))
            }
        }
    }
}