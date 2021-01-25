package tz.co.asoft

fun Map<String, *>.toKtorDaoOptions(): KtorDaoOptions {
    val url: String by this
    val version: String by this
    val headers = getOrElse("headers") { mapOf<String, String>() } as Map<String, String>
    return KtorDaoOptions(url, version, headers)
}