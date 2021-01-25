package tz.co.asoft

import kotlinx.css.BorderStyle
import kotlinx.css.Color
import kotlinx.css.JustifyContent
import kotlinx.css.properties.border
import kotlinx.css.px
import kotlinx.html.js.onClickFunction
import react.RBuilder
import styled.css

fun RBuilder.DoerPane(doers: List<Doer>, onDoerClicked: (Doer) -> Unit, onDoerCreated: (Doer) -> Unit) = Grid {
    Form {
        Grid("2fr 1fr auto") {
            TextInput(name = "name")
            Grid("1fr 1fr") {
                Switch("perm", value = "todo.todo.create", label = "Create")
                Switch("perm", value = "todo.todo.edit", label = "Edit")
            }
            Button("Create")
        }
    } onSubmit {
        val name by text()
        val perm by multi()
        val doer = Doer(name = name, perms = perm)
        onDoerCreated(doer)
    }

    Grid(doers.joinToString(separator = " ") { "1fr" }) {
        doers.forEach { doer ->
            FlexBox { theme ->
                attrs.onClickFunction = { onDoerClicked(doer) }
                css {
                    justifySelf = JustifyContent.center
                    if (doer.selected) {
                        border(2.px, BorderStyle.solid, theme.primaryColor)
                    } else {
                        border(2.px, BorderStyle.solid, Color.transparent)
                    }
                }
                var display = doer.name + "["
                if (doer.perms.contains("todo.todo.create")) display += "C"
                if (doer.perms.contains("todo.todo.edit")) display += "E"
                display += "]"
                +display
            }
        }
    }
}