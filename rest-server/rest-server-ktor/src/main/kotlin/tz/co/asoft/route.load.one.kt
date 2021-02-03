package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.nullable

internal suspend fun <T : Entity> IRestModule<T>.loadOne(
    call: ApplicationCall,
    action: String = "load entity",
    log: Logger,
    permit: ISystemPermission,
    permitValue: String
) = try {
    val id = call.parameters["uid"].toString()
    when (val state = authorize(call, action, log, permit, permitValue)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val entity = controller.load(id).await()
            val res = Success(entity)
            send(call, log, if (entity != null) HttpStatusCode.Found.value else HttpStatusCode.NotFound.value, serializer.nullable, res)
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError.value, serializer, e.toFailure())
}