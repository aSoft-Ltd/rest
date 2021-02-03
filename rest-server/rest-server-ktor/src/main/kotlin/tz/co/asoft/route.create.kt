package tz.co.asoft

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.create(
    call: ApplicationCall,
    log: Logger,
    action: String = "create entity",
    permit: ISystemPermission,
    permitValue: String
) = try {
    when (val state = authorize(call, action, log, permit, permitValue)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val res = Success(controller.create(call.getEntitiesFromBody(serializer)).await())
            send(call, log, HttpStatusCode.Created.value, ListSerializer(serializer), res)
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError.value, serializer, e.toFailure())
}