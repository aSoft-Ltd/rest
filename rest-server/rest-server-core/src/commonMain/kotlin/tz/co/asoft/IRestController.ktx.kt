package tz.co.asoft

suspend fun <T : Entity> IRestController<T>.authorize(
    token: String?,
    log: Logger,
    keyFetcher: KeyFetcher,
    verifier: (SecurityKey) -> JWTVerifier,
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

    if (verifier(key).verify(jwt) is JWTVerification.Invalid) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "jwt sent was invalid"
        )
        log.warn("An Invalid jwt (token=$token)")
        return AuthorizationState.UnAuthorized(401, res)
    }

    val authZ = AuthorizationState.Authorized.parseOrNull<T>(jwt)
    if (authZ == null) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "Failed to convert jwt into a known Authorization state"
        )
        log.warn("jwt (token=$token) tried to access data and the server failed to parse it")
        return AuthorizationState.UnAuthorized(401, res)
    }

    val principle = authZ.principle

    if (!principle.has(permit)) {
        val res = Result.Failure<T>(
            error = "Failed to $action",
            type = "Unauthorized",
            reason = "JWT has no permission that ${permit.details}"
        )
        log.warn("A valid token ($token), tried to $action while it is not permitted to do so")
        return AuthorizationState.UnAuthorized(401, res)
    }
    return authZ
}