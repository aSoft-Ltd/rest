package tz.co.asoft

import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.s
import kotlinx.css.properties.transition
import kotlinx.html.js.onClickFunction
import react.RBuilder
import styled.css
import styled.styledDiv

fun RBuilder.Todo(todo: Todo?, onToggled: () -> Unit, onDelete: () -> Unit) = Grid("1fr 6fr 1fr") {
    Switch(
        name = "",
        value = if (todo?.deleted?.not() == true) "completed" else "uncompleted",
        checked = todo?.deleted?.not() == true,
        onChanged = { onToggled() }
    )
    TodoContent(todo)
    Grid {
        if (todo != null) {
            attrs.onClickFunction = { onDelete() }
        }
        FaTrash {}
    }
}

private fun RBuilder.TodoContent(todo: Todo?) = Grid { theme ->
    css {
        children {
            justifySelf = JustifyContent.stretch
            alignSelf = Align.stretch
            transition(duration = 0.2.s)
        }
    }
    if (todo == null) {
        styledDiv {
            css {
                margin(0.2.em)
                backgroundColor = theme.onSurfaceColor.withAlpha(0.3)
            }
        }
    } else {
        styledDiv {
            css {
                padding(0.2.em)
                backgroundColor = Color.transparent
            }
            +todo.details
        }
    }
}