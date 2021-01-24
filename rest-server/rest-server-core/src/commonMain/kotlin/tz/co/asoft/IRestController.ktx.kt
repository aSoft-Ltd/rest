package tz.co.asoft

suspend fun <T : Entity> IRestController<T>.authorize(
    token: String?,
    log: Logger,
    keyFetcher: KeyFetcher,
    verifier: JWTVerifier,
    action: String,
    origin: String,
    permit: ISystemPermission
): AuthorizationState<T> {
    log.info("Authorizing request from $origin with token $token")
    if (token == null) {
        val res = Failure(
            error = "Failed to $action",
            type = "Bad Request",
            reason = "No Provided jwt in Authorization in the header"
        ).toResult<T>()
        return AuthorizationState.UnAuthorized(400, res)
    }

    val jwt = JWT.parseOrNull(token)

    if (jwt == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to parse jwt token"
        )
        log.warn("Failed to parse jwt (token=$token)")
        return AuthorizationState.UnAuthorized(401, res)
    }

    val key = keyFetcher.load(jwt.header.kid)

    if (key == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to find key (kid=${jwt.header.kid})"
        )
        log.warn("Failed to find key (kid=${jwt.header.kid}) to verify jwt")
        return AuthorizationState.UnAuthorized(401, res)
    }

    if (verifier.verify(jwt) is JWTVerification.Invalid) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to find validate jwt"
        )
        log.warn("An Invalid jwt (token=$token)")
        return AuthorizationState.UnAuthorized(401, res)
    }

    // DELETE ONLY IF YOU ARE CERTAIN YOU WONT AUTHORIZE SPECIFIC HOSTS
//    val hosts = jwt.payload.hostsOrNull ?: listOf()
//
//    if (!hosts.contains(call.request.origin.url)) {
//        val res = Result.Failure<T>(
//            error = "Failed to $action",
//            type = "Unauthorized",
//            reason = "$jwt isn't configured to access data from ${call.request.origin.url}"
//        )
//        log.warn("A valid $jwt tried to $action while it is not permitted to do so from ${call.request.origin.url}")
//        return AuthorizationState.UnAuthorized(401, res)
//    }

    val authZ = AuthorizationState.Authorized.parseOrNull<T>(jwt)
    if (authZ == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to convert jwt into Authorization state"
        )
        log.warn("jwt (token=$token) tried to access data and the server failed to parse it")
        return AuthorizationState.UnAuthorized(401, res)
    }

    val claims = authZ.principle.claims
    if (!claims.contains(permit.title)) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Token $token has no permission that ${permit.details}"
        )
        log.warn("A valid token ($token), tried to $action while it is not permitted to do so")
        return AuthorizationState.UnAuthorized(401, res)
    }
    return authZ
}