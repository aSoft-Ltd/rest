package tz.co.asoft

data class Doer(
    override var uid: String? = null,
    override val name: String,
    val selected: Boolean = false,
    val perms: List<String> = listOf(),
    override var deleted: Boolean = false
) : NamedEntity