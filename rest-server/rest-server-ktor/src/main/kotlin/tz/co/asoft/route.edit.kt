package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.edit(call: ApplicationCall, log: Logger, permit: ISystemPermission) = try {
    when (val state = authorize(call, "edit entity", log, permit)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val res = Success(controller.edit(call.getEntitiesFromBody(serializer)).await())
            send(call, log, HttpStatusCode.OK.value, ListSerializer(serializer), res)
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError.value, serializer, e.toFailure())
}