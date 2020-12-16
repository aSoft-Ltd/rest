package tz.co.asoft

import io.ktor.application.call
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

open class RestServer(
    val port: Int = 8080,
    val log: Logger,
    val modules: List<IRestModule<*>>
) {
    open fun start() = embeddedServer(CIO, port) {
        installCORS()
        modules.forEach {
            it.setRoutes(this, log)
            log.info("Endpoints at: ${it.path}")
        }
        routing {
            get("/status") {
                call.respondText("Healthy")
            }
        }
    }.start(wait = true)
}