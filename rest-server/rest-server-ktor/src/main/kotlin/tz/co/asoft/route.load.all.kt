package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer

internal suspend fun <T : Entity> IRestModule<T>.loadAll(call: ApplicationCall, log: Logger, permit: ISystemPermission) = try {
//    when (val state = authorize(call, log, "load all entities", ":read")) {
//        is AuthorizationState.UnAuthorized -> {
//            send(call, log, state.code, serializer, state.res)
//        }
//        is AuthorizationState.Authorized -> {
//            val res = Success(controller.all())
//            send(call, log, HttpStatusCode.OK, ListSerializer(serializer), res)
//        }
//    }
    val res = Success(controller.all().await())
    send(call, log, HttpStatusCode.OK, ListSerializer(serializer), res)
} catch (e: Throwable) {
    log.failure(e)
    send(call, log, HttpStatusCode.InternalServerError, serializer, e.toFailure())
}