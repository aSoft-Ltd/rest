package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.loadAll(
    call: ApplicationCall,
    action: String = "load all entities",
    log: Logger,
    permit: ISystemPermission,
    permitValue: String
) = try {
    when (val state = authorize(call, action, log, permit, permitValue)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val res = Success(controller.all().await())
            send(call, log, HttpStatusCode.OK.value, ListSerializer(serializer), res)
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError.value, serializer, e.toFailure())
}