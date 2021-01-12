package tz.co.asoft

import kotlinx.browser.document
import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.dom.render
import react.router.dom.browserRouter
import styled.css
import styled.styledDiv

val kfg by lazy { konfig() }

fun main() = document.getElementById("root").setContent {
    TodoApp.configureDao(TodoAppDao(InMemoryDao("doer"), InMemoryDao("todo")))
    browserRouter {
        ThemeProvider {
            TodoApp()
        }
    }
}