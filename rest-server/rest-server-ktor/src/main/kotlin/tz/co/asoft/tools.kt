package tz.co.asoft

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*

val RequestConnectionPoint.url get() = "$scheme://$remoteHost:$port"

fun ApplicationCall.authorizationToken() = request.authorization()?.split(" ")?.getOrNull(1)