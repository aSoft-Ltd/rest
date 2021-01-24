package tz.co.asoft

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.routing.*
import kotlinx.serialization.KSerializer

open class RestModule<T : Entity>(
    override val version: String,
    override val root: String,
    override val subRoot: String?,
    override val keyFetcher: KeyFetcher,
    override val verifier: JWTVerifier,
    override val serializer: KSerializer<T>,
    override val controller: IRestController<T>,
    override val readPermission: ISystemPermission,
    override val createPermission: ISystemPermission,
    override val updatePermission: ISystemPermission,
    override val deletePermission: ISystemPermission,
    override val wipePermission: ISystemPermission,
) : IRestModule<T> {
    override fun setRoutes(app: Application, log: Logger) = app.routing {
        post(path = path) {
            log.info("Creating entity at $path")
            create(call, log, createPermission)
        }

        post("$path/load") {
            log.info("Loading entity at $path/load")
            loadMany(call, log, readPermission)
        }

        patch(path = path) {
            log.info("Updating entity at $path")
            edit(call, log, updatePermission)
        }

        get(path = "$path/page") {
            log.info("Loading paged data at $path")
            page(call, log, readPermission)
        }

        get("$path/{uid}") {
            log.info("Loading entity at $path/{uid}")
            loadOne(call, log, readPermission)
        }

        delete(path = path) {
            log.info("Deleting entity at $path")
            delete(call, log, deletePermission)
        }

        delete("$path/wipe") {
            log.info("Wiping entities at $path/wipe")
            wipe(call, log, wipePermission)
        }

        get("$path/all") {
            log.info("Loading all entities at $path/all")
            loadAll(call, log, readPermission)
        }

        get("$path/all-deleted") {
            log.info("Loading all deleted entities at $path/all-deleted")
            loadAllDeleted(call, log, readPermission)
        }
    }
}
