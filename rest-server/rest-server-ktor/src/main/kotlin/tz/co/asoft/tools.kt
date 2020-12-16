package tz.co.asoft

import io.ktor.http.*

val RequestConnectionPoint.url get() = "$scheme://$remoteHost:$port"
