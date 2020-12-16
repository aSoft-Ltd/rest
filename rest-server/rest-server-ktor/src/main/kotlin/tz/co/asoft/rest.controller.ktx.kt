package tz.co.asoft

import io.ktor.application.ApplicationCall
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import kotlinx.serialization.KSerializer

suspend fun <T> send(
    call: ApplicationCall,
    log: Logger,
    code: HttpStatusCode,
    serializer: KSerializer<T>,
    res: Result<T>
) = call.respondText(Result.stringify(serializer, res), ContentType.Application.Json, status = code).also {
    when (res.status) {
        ResultStatus.Success -> when (val data = res.success()) {
            is Entity -> log.info("Sent entity with uid: ${data.uid} successfully")
            is Collection<*> -> log.info("Sent ${data.size} entities successfully")
            else -> log.info("Response sent successfully")
        }
        ResultStatus.Failure -> log.warn("Sending error msg: ${res.failure()}")
    }
}