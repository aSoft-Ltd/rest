package tz.co.asoft

interface KeyFetcher {
    suspend fun load(kid: String): SecurityKey?
    suspend fun all(): List<SecurityKey>
}