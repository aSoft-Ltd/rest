package tz.co.asoft

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tz.co.asoft.TodoAppViewModel.Intent
import tz.co.asoft.TodoAppViewModel.State

class TodoAppViewModel(private val todoRepo: IRepo<Todo>) : VModel<Intent, State>(State.Loading("Fetching todos")) {
    sealed class State {
        class Loading(val msg: String) : State()
        class Todos(val data: List<Todo>) : State()
        class Error(val msg: String) : State()
    }

    sealed class Intent {
        object ViewTodos : Intent()
        class AddTodo(val todo: Todo) : Intent()
        class ToggleTodo(val todo: Todo) : Intent()
    }

    override fun execute(i: Intent) = when (i) {
        Intent.ViewTodos -> viewTodos()
        is Intent.AddTodo -> addTodo(i)
        is Intent.ToggleTodo -> toggleTodo(i)
    }

    private fun viewTodos() = launch {
        flow {
            emit(State.Loading("Fetching todos"))
            emit(State.Todos(todoRepo.all()))
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
    }

    private fun addTodo(i: Intent.AddTodo) = launch {
        flow {
            emit(State.Loading("Adding todo [${i.todo.details}] "))
            todoRepo.create(i.todo)
            emit(State.Todos(todoRepo.all()))
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
    }

    private fun toggleTodo(i: Intent.ToggleTodo) = launch {
        flow {
            emit(State.Loading("Changing todo [${i.todo.details}] "))
            todoRepo.edit(i.todo.copy(deleted = !i.todo.deleted))
            emit(State.Todos(todoRepo.all()))
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
    }
}