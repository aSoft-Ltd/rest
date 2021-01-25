package tz.co.asoft

import io.ktor.application.Application
import io.ktor.routing.Routing
import kotlinx.serialization.KSerializer

interface IRestModule<T : Entity> {
    val version: String
    val root: String
    val subRoot: String?
    val keyFetcher: KeyFetcher
    val verifier: (SecurityKey)->JWTVerifier
    val path get() = "/$version/$root" + if (subRoot != null) "/$subRoot" else ""
    val serializer: KSerializer<T>
    val controller: IRestController<T>
    fun setRoutes(app: Application, log: Logger): Routing

    /*
    Permissions
     */
    val readPermission: ISystemPermission
    val createPermission: ISystemPermission
    val updatePermission: ISystemPermission
    val deletePermission: ISystemPermission
    val wipePermission: ISystemPermission
}