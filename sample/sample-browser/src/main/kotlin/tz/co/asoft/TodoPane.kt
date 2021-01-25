package tz.co.asoft

import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.RBuilder
import styled.css

fun RBuilder.TodoPane(
    todos: List<Todo>,
    onToggled: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    onTodoCreated: (Todo) -> Unit
) = Grid {
    Grid { theme ->
        css { +theme.text.h3.clazz }
        +"Todo App"
    }
    if (todos.isEmpty()) Todo(null, {}, {}) else todos.forEach {
        Todo(todo = it, onToggled = { onToggled(it) }, onDelete = { onDelete(it) })
    }
    PostEditor(onSubmit = { onTodoCreated(Todo(details = it)) })
}