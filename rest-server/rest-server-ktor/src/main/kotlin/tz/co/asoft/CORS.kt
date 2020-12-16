package tz.co.asoft

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.maxAgeDuration
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlin.time.ExperimentalTime
import kotlin.time.days

fun Application.installCORS() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        header(HttpHeaders.XForwardedProto)
        anyHost()
        allowCredentials = true
        maxAgeInSeconds = 1000
        allowNonSimpleContentTypes = true
    }
}