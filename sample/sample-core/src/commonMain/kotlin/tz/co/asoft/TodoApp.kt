package tz.co.asoft

object TodoApp : DaoFactory<TodoAppDao>() {
    object repo {
        fun todo() = repo { Repo(dao.todo) }
        fun doer() = repo { Repo(dao.doer) }
    }

    object viewmodel {
        fun todoApp() = TodoAppViewModel(repo.doer(), repo.todo())
    }
}