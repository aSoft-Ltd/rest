package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.nullable

internal suspend fun <T : Entity> IRestModule<T>.loadOne(call: ApplicationCall, log: Logger, permit: ISystemPermission) = try {
    val id = call.parameters["uid"].toString()
    when (val state = authorize(call, log, "load entity(uid=$id)", permit)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val entity = controller.load(id).await()
            val res = Success(entity)
            send(call, log, if (entity != null) HttpStatusCode.Found else HttpStatusCode.NotFound, serializer.nullable, res)
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError, serializer, e.toFailure())
}