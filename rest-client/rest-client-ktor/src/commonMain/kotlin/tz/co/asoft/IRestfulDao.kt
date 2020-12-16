package tz.co.asoft

import io.ktor.client.HttpClient
import kotlinx.serialization.KSerializer

interface IRestfulDao<T : Entity> : IDao<T> {
    val client: HttpClient
    val options: RestfulOptions
    val root: String
    val subRoot: String?
    val serializer: KSerializer<T>

    val path get() = "${options.url}/${options.version}/$root" + if (subRoot != null) "/$subRoot" else ""
}