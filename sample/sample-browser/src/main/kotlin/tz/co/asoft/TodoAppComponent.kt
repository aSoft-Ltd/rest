package tz.co.asoft

import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.RBuilder
import react.RProps
import styled.css
import tz.co.asoft.TodoAppViewModel.Intent
import tz.co.asoft.TodoAppViewModel.State

class TodoAppComponent : VComponent<RProps, Intent, State, TodoAppViewModel>() {
    override val viewModel = TodoApp.viewmodel.todoApp()

    override fun componentDidMount() {
        super.componentDidMount()
        post(Intent.ViewTodos)
    }

    private fun RBuilder.DoerTodos(doers: List<Doer>, todos: List<Todo>) = Grid {
        css {
            position = Position.absolute
            left = 100.px
            top = 100.px
            width = 400.px
            padding(0.5.em)
            boxShadow(Color.gray, blurRadius = 2.px, spreadRadius = 1.px)
        }

        DoerPane(
            doers = doers,
            onDoerClicked = { post(Intent.SelectDoer(it)) },
            onDoerCreated = { post(Intent.CreateDoer(it)) }
        )

        TodoPane(
            todos = todos,
            onToggled = { post(Intent.ToggleTodo(it)) },
            onDelete = { post(Intent.DeleteTodo(it)) },
            onTodoCreated = { post(Intent.CreateTodo(it)) }
        )
    }

    override fun RBuilder.render(ui: State) = when (ui) {
        is State.Loading -> Loader(ui.msg)
        is State.DoerTodos -> DoerTodos(ui.doers, ui.todos)
        is State.Error -> Error(ui.msg)
    }
}

fun RBuilder.TodoApp() = child(TodoAppComponent::class) {}