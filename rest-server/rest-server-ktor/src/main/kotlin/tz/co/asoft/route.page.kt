package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.page(call: ApplicationCall, log: Logger, permit: ISystemPermission) = try {
    when (val state = authorize(call, log, "load page", permit)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val params = call.request.queryParameters
            val size = params["size"]?.toIntOrNull()
            if (size != null) {
                val no = params["no"]?.toIntOrNull() ?: 1
                val res: Result<List<T>> = Success(controller.page(no, size).await())
                send(call, log, HttpStatusCode.OK, ListSerializer(serializer), res)
            } else {
                val res = Failure(
                    error = "Failed to load page",
                    type = "Insufficient parameters",
                    reason = "get request has no size parameter. Make sure you have `?size=10` in url"
                ).toResult<T>()
                send(call, log, HttpStatusCode.BadRequest, serializer, res)
            }
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError, serializer, e.toFailure())
}