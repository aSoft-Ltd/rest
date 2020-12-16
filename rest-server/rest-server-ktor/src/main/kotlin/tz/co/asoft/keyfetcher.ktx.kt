package tz.co.asoft

private class Fetcher(private val source: KeySource<*>) : KeyFetcher {
    override suspend fun load(kid: String) = when (val key = source.load(kid)) {
        is SecurityKeyPair? -> key?.publicSecurityKey
        is SecurityKey? -> key
        else -> null
    }

    override suspend fun all() = source.all().map { it.publicKey }
}

val KeyInfo.publicKey
    get() = when (this) {
        is SecurityKey -> this
        is SecurityKeyPair -> publicSecurityKey
        else -> error("Unkown key")
    }

val KeySource<*>.fetcher: KeyFetcher get() = Fetcher(this)