package tz.co.asoft

fun Map<String, *>.toRestfulOptions(): RestfulOptions {
    val url: String by this
    val version: String by this
    val headers = getOrElse("headers") { mapOf<String, String>() } as Map<String, String>
    return RestfulOptions(url, version, headers)
}