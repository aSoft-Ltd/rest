package tz.co.asoft

import kotlinx.serialization.json.Json

sealed class AuthorizationState<T> {
    class Authorized<T>(val principle: IPrinciple) : AuthorizationState<T>() {
        companion object {
            fun <T> parse(jwt: JWT): Authorized<T> {
                val accountMap = jwt.payload["account"] as? Map<String, Any> ?: throw Exception("JWT misses an account object")
                val account = Json.decodeFromString(UserAccount.serializer(), Mapper.encodeToString(accountMap))
                return when {
                    jwt.payload["user"] != null -> {
                        val userMap = jwt.payload["user"] as? Map<String, Any> ?: throw Exception("JWT misses a user object")
                        val user = Json.decodeFromString(User.serializer(), Mapper.encodeToString(userMap))
                        val principle = object : IUserPrinciple {
                            override val account = account
                            override val claims = jwt.payload.claimsOrNull ?: mapOf()
                            override val user = user
                        }
                        Authorized(principle)
                    }
                    jwt.payload["app"] != null -> {
                        val appMap = jwt.payload["app"] as? Map<String, Any> ?: throw Exception("JWT misses an app object")
                        val app = Json.decodeFromString(ClientApp.serializer(), Mapper.encodeToString(appMap))
                        val principle = object : IClientAppPrinciple {
                            override val account = account
                            override val claims = jwt.payload.claimsOrNull ?: mapOf()
                            override val app = app
                        }
                        Authorized(principle)
                    }
                    else -> throw Exception("JWT doesn't contain app or user object")
                }
            }

            fun <T> parseOrNull(jwt: JWT): Authorized<T>? = try {
                parse(jwt)
            } catch (_: Throwable) {
                null
            }
        }
    }

    class UnAuthorized<T>(val code: Int, val res: Result<T>) : AuthorizationState<T>()
}