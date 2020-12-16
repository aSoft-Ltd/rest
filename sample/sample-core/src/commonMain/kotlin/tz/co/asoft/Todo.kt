package tz.co.asoft

data class Todo(
    override var uid: String?,
    val details: String,
    override var deleted: Boolean
) : Entity