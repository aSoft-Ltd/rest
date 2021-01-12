package tz.co.asoft

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer

open class RestfulDao<T : Entity>(
    override val options: RestfulOptions,
    override val serializer: KSerializer<T>,
    override val scope: CoroutineScope = CoroutineScope(SupervisorJob()),
    override val root: String,
    override val subRoot: String?,
    override var token: String?,
    override val client: HttpClient
) : IRestfulDao<T> {
    fun HttpRequestBuilder.appendHeaders() {
        options.headers.forEach { (k, v) ->
            header(k, v)
        }
        if (token != null) header("Authorization", "Bearer $token")
    }

    override fun create(list: Collection<T>) = scope.later {
        val json = client.post<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun create(t: T) = scope.later {
        create(listOf(t)).await().first()
    }

    override fun edit(list: Collection<T>) = scope.later {
        val json = client.patch<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun edit(t: T) = scope.later {
        edit(listOf(t)).await().first()
    }

    override fun delete(list: Collection<T>) = scope.later {
        val json = client.delete<String>(path) {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun delete(t: T) = scope.later {
        delete(listOf(t)).await().first()
    }

    override fun wipe(list: Collection<T>) = scope.later {
        val json = client.delete<String>("$path/wipe") {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(serializer), list.toList())
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun wipe(t: T) = scope.later {
        wipe(listOf(t)).await().first()
    }

    override fun load(uids: Collection<String>) = scope.later {
        val stringIds = uids.filter { it.isNotBlank() }
        if (stringIds.isEmpty()) return@later emptyList()
        val json = client.post<String>("$path/load") {
            appendHeaders()
            body = EJson.encodeToString(ListSerializer(String.serializer()), stringIds)
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun load(uid: String) = scope.later {
        val json = client.get<String>("$path/$uid") {
            appendHeaders()
        }
        Result.parse(serializer.nullable, json).response()
    }

    override fun page(no: Int, size: Int) = scope.later {
        require(no > 0) { "Page Numbering starts from 1" }
        val query = "no=$no&size=$size"
        val json = client.get<String>("$path/page?$query") {
            appendHeaders()
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun all() = scope.later {
        val json = client.get<String>("$path/all") {
            appendHeaders()
        }
        Result.parse(ListSerializer(serializer), json).response()
    }

    override fun allDeleted() = scope.later {
        val json = client.get<String>("$path/all-deleted") {
            appendHeaders()
        }
        Result.parse(ListSerializer(serializer), json).response()
    }
}