package tz.co.asoft

import kotlinx.serialization.Serializable

@Serializable
class RestfulPageRequestInfo(val key: VKey, val pageSize: Int)