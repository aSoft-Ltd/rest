package tz.co.asoft

fun Map<String, *>.toKtorOptions(): KtorOptions {
    val port: String by this
    return KtorOptions(port.toInt())
}