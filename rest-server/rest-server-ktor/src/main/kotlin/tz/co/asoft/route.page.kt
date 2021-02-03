package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.page(
    call: ApplicationCall,
    log: Logger,
    action: String = "load page",
    permit: ISystemPermission,
    permitValue: String
) = try {
    when (val state = authorize(call, action, log, permit, permitValue)) {
        is AuthorizationState.UnAuthorized -> {
            send(call, log, state.code, serializer, state.res)
        }
        is AuthorizationState.Authorized -> {
            val params = call.request.queryParameters
            val size = params["size"]?.toIntOrNull()
            if (size != null) {
                val no = params["no"]?.toIntOrNull() ?: 1
                val res: Result<List<T>> = Success(controller.page(no, size).await())
                send(call, log, HttpStatusCode.OK.value, ListSerializer(serializer), res)
            } else {
                val res = Failure(
                    error = "Failed to load page",
                    type = "Insufficient parameters",
                    reason = "get request has no size parameter. Make sure you have `?size=10` in url"
                ).toResult<T>()
                send(call, log, HttpStatusCode.BadRequest.value, serializer, res)
            }
        }
    }
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError.value, serializer, e.toFailure())
}