package tz.co.asoft

import io.ktor.application.*
import io.ktor.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

suspend fun ApplicationCall.getIdsFromBody(): List<String> {
    return EJson.decodeFromString(ListSerializer(String.serializer()), receiveText())
}

suspend fun <T> ApplicationCall.getEntitiesFromBody(serializer: KSerializer<T>): List<T> {
    return EJson.decodeFromString(ListSerializer(serializer), receiveText())
}