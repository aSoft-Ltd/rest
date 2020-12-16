pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}

rootProject.name = "rest"

include(":rest-server-core")
project(":rest-server-core").projectDir = File("rest-server/rest-server-core")
include(":rest-server-ktor")
project(":rest-server-ktor").projectDir = File("rest-server/rest-server-ktor")

include(":rest-client-ktor")
project(":rest-client-ktor").projectDir = File("rest-client/rest-client-ktor")

include(":sample-browser")
project(":sample-browser").projectDir = File("sample/sample-browser")
include(":sample-core")
project(":sample-core").projectDir = File("sample/sample-core")
