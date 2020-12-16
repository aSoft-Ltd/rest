package tz.co.asoft

import io.ktor.application.ApplicationCall
import io.ktor.features.origin
import io.ktor.http.HttpStatusCode
import io.ktor.request.authorization
import io.ktor.util.KtorExperimentalAPI
import tz.co.asoft.JWTVerification.Invalid

@OptIn(KtorExperimentalAPI::class)
suspend fun <T : Entity> IRestModule<T>.authorize(
    call: ApplicationCall,
    log: Logger,
    action: String,
    permit: String
): AuthorizationState<T> {
    val token = call.request.authorization()?.split(" ")?.getOrNull(1)
    log.info("Authorizing request from ${call.request.origin.url} with api key $token")
    if (token == null) {
        val res = Failure(
            error = "Failed to $action",
            type = "Bad Request",
            reason = "No Provided jwt in Authorization in the header"
        ).toResult<T>()
        return AuthorizationState.UnAuthorized(HttpStatusCode.BadRequest, res)
    }

    val jwt = JWT.parseOrNull(token)

    if (jwt == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to parse jwt token"
        )
        log.warn("Failed to parse jwt (token=$token) which tried to access data at $path")
        return AuthorizationState.UnAuthorized(HttpStatusCode.Unauthorized, res)
    }

    val key = keyFetcher.load(jwt.header.kid)

    if (key == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to find key (kid=${jwt.header.kid})"
        )
        log.warn("Failed to parse jwt (token=$token) which tried to access data at $path")
        return AuthorizationState.UnAuthorized(HttpStatusCode.Unauthorized, res)
    }

    if (jwt.verifyRS512(key) is Invalid) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to find validate jwt"
        )
        log.warn("An Invalid jwt (token=$token) tried to access data at $path")
        return AuthorizationState.UnAuthorized(HttpStatusCode.Unauthorized, res)
    }

    val hosts = jwt.payload.hostsOrNull ?: listOf()

    if (!hosts.contains(call.request.origin.url)) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "$jwt isn't configured to access data from ${call.request.origin.url}"
        )
        log.warn("A valid $jwt tried to $action while it is not permitted to do so from ${call.request.origin.url}")
        return AuthorizationState.UnAuthorized(HttpStatusCode.Unauthorized, res)
    }

//    val claims = jwt.payload.claimsOrNull ?: listOf()
//    if (!api.permitsFor(root, subRoot)suspend .contains(permit)) {
//        val res = Result.Failure<T>(
//            msg = "Failed to $action",
//            type = "Unauthorized",
//            reason = "api(key=$apiKey,name=${api.name}) has no permission to $action"
//        )
//        log.warn("A valid api(key=$apiKey,name=${api.name}) tried to $action while it is not permitted to do so")
//        return AuthorizationState.UnAuthorized(HttpStatusCode.Unauthorized, res)
//    }
    return AuthorizationState.Authorized()
}