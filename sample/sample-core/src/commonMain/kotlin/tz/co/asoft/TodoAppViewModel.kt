package tz.co.asoft

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tz.co.asoft.TodoAppViewModel.Intent
import tz.co.asoft.TodoAppViewModel.State

class TodoAppViewModel(
    private val doerRepo: IRepo<Doer>,
    private val todoRepo: IRepo<Todo>
) : VModel<Intent, State>(State.Loading("Fetching todos")) {
    sealed class State {
        class Loading(val msg: String) : State()
        class DoerTodos(val doers: List<Doer>, val todos: List<Todo>) : State()
        class Error(val msg: String) : State()
    }

    sealed class Intent {
        object ViewTodos : Intent()
        class CreateTodo(val todo: Todo) : Intent()
        class ToggleTodo(val todo: Todo) : Intent()
        class DeleteTodo(val todo: Todo) : Intent()
        class CreateDoer(val doer: Doer) : Intent()
        class SelectDoer(val doer: Doer) : Intent()
    }

    override fun execute(i: Intent) = when (i) {
        Intent.ViewTodos -> showDoersTodos()
        is Intent.CreateTodo -> addTodo(i)
        is Intent.ToggleTodo -> toggleTodo(i)
        is Intent.DeleteTodo -> deleteTodo(i)
        is Intent.CreateDoer -> createDoer(i)
        is Intent.SelectDoer -> selectDoer(i)
    }

    private fun selectDoer(i: Intent.SelectDoer) = launch {
        flow<State> {
            emit(State.Loading("Selecting doer"))
            val doers = doerRepo.all().map { it.copy(selected = false) }
            doerRepo.edit(doers)
            doerRepo.all().find { it.uid == i.doer.uid }?.let {
                doerRepo.edit(it.copy(selected = true))
            }
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
        showDoersTodos()
    }

    private fun showDoersTodos() = launch {
        flow {
            emit(State.Loading("Fetching Doers and Todos"))
            val (doers, todos) = coroutineScope {
                val doers = async { doerRepo.all() }
                val todos = async { todoRepo.all() }
                doers.await() to todos.await()
            }
            emit(State.DoerTodos(doers, todos))
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
    }

    private fun createDoer(i: Intent.CreateDoer) = launch {
        flow<State> {
            emit(State.Loading("Creating doer"))
            doerRepo.create(i.doer)
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
        showDoersTodos()
    }

    private fun deleteTodo(i: Intent.DeleteTodo) = launch {
        flow<State> {
            emit(State.Loading("Deleting todo"))
            todoRepo.wipe(i.todo)
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
        showDoersTodos()
    }

    private fun addTodo(i: Intent.CreateTodo) = launch {
        flow<State> {
            emit(State.Loading("Adding todo [${i.todo.details}] "))
            todoRepo.create(i.todo)
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
        showDoersTodos()
    }

    private fun toggleTodo(i: Intent.ToggleTodo) = launch {
        flow<State> {
            emit(State.Loading("Changing todo [${i.todo.details}] "))
            todoRepo.edit(i.todo.copy(deleted = !i.todo.deleted))
        }.catch {
            emit(State.Error("Failed to load todos: ${it.message}"))
        }.collect { ui.value = it }
        showDoersTodos()
    }
}