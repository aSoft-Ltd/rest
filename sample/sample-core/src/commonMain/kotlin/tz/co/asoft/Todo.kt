package tz.co.asoft

data class Todo(
    override var uid: String? = null,
    val details: String,
    override var deleted: Boolean = false
) : Entity