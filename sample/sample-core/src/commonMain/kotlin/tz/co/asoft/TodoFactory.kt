package tz.co.asoft

class TodoAppDao(
    val doer: IDao<Doer>,
    val todo: IDao<Todo>
)