package tz.co.asoft

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer

open class RestfulDao<T : Entity>(
    override val options: RestfulOptions,
    override val serializer: KSerializer<T>,
    override val root: String,
    override val subRoot: String?,
    override val client: HttpClient
) : IRestfulDao<T> {

    fun HttpRequestBuilder.appendHeaders() {
        options.headers.forEach { (k, v) ->
            header(k, v)
        }
    }

    override suspend fun create(list: Collection<T>): List<T> {
        val json = client.post<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun create(t: T) = create(listOf(t)).first()

    override suspend fun edit(list: Collection<T>): List<T> {
        val json = client.patch<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun edit(t: T) = edit(listOf(t)).first()

    override suspend fun delete(list: Collection<T>): List<T> {
        val json = client.delete<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun delete(t: T) = delete(listOf(t)).first()

    override suspend fun wipe(list: Collection<T>): List<T> {
        val json = client.delete<String>("$path/wipe") {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun wipe(t: T): T = wipe(listOf(t)).first()

    override suspend fun load(uids: Collection<String>): List<T> {
        val stringIds = uids.filter { it.isNotBlank() }
        if (stringIds.isNotEmpty()) {
            return emptyList()
        }
        val json = client.post<String>("$path/load") {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(String.serializer()), stringIds)
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun load(uid: String): T? {
        val json = client.get<String>("$path/$uid") {
            appendHeaders()
        }
        return Result.parse(serializer.nullable, json).response()
    }

//    override suspend fun load(startAt: String?, limit: Int): List<T> {
//        var query = "size=$limit"
//        if (startAt != null) {
//            query += "&startAt=$startAt"
//        }
//        val json = client.get<String>("$path/page?$query") {
//            appendHeaders()
//        }
//        return Result.parse(ListSerializer(serializer), json).response()
//    }

    override suspend fun all(): List<T> {
        val json = client.get<String>("$path/all") {
            appendHeaders()
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

    override suspend fun allDeleted(): List<T> {
        val json = client.get<String>("$path/all-deleted") {
            appendHeaders()
        }
        return Result.parse(ListSerializer(serializer), json).response()
    }

//    override fun pageLoader(predicate: (T) -> Boolean): PageLoader<*, T> = RestfulPageLoader(this, predicate)
}