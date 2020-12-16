package tz.co.asoft

import io.ktor.http.HttpStatusCode

sealed class AuthorizationState<T> {
    class Authorized<T> : AuthorizationState<T>()
    class UnAuthorized<T>(val code: HttpStatusCode, val res: Result<T>) : AuthorizationState<T>()
}