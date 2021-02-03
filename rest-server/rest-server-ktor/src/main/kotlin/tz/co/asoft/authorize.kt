package tz.co.asoft

import io.ktor.application.ApplicationCall
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.authorization
import io.ktor.util.KtorExperimentalAPI
import tz.co.asoft.JWTVerification.Invalid

suspend fun <T : Entity> IRestModule<T>.authorize(
    call: ApplicationCall,
    action: String,
    log: Logger,
    permit: ISystemPermission,
    permitValue: String
): AuthorizationState<T> {
    val token = call.authorizationToken()
    val origin = call.request.origin.url
    return authorize(
        token = token,
        log = log,
        keyFetcher = keyFetcher,
        verifier = verifier,
        action = action,
        origin = origin,
        permit = permit,
        permitValue = permitValue
    )
}