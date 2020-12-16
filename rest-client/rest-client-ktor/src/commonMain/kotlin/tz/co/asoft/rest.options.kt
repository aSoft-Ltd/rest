package tz.co.asoft

fun Map<String, *>.toRestfulOptions(): RestfulOptions {
    val url: String by this
    val version: String by this
    val headers: Map<String, String> by this
    return RestfulOptions(url, version, headers)
}