plugins {
    kotlin("js")
    id("tz.co.asoft.applikation")
}

group = "tz.co.asoft.rest"
version = vers.asoft.rest

applikation {
    debug()
    release()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig { cssSupport.enabled = true }
        }
        binaries.executable()
    }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":sample-core"))
                implementation("tz.co.asoft:applikation-runtime:${vers.asoft.builders}")
                implementation(asoft("reakt-navigation", vers.asoft.reakt))
                implementation(asoft("reakt-text", vers.asoft.reakt))
                implementation(asoft("reakt-inputs", vers.asoft.reakt))
                implementation(asoft("reakt-composites", vers.asoft.reakt))
                implementation(asoft("reakt-tables", vers.asoft.reakt))
                implementation(asoft("rich-text-editor-react", "0.0.1"))
                implementation(asoft("viewmodel-react", vers.asoft.viewmodel))
                implementation(asoft("persist-inmemory", vers.asoft.persist))
                implementation(asoft("form-react", vers.asoft.form))
            }
        }
    }
}

