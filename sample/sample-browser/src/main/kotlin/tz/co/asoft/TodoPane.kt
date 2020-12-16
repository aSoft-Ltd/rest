package tz.co.asoft

import kotlinx.css.*
import kotlinx.css.Color
import kotlinx.css.properties.boxShadow
import react.RBuilder
import styled.css

fun RBuilder.TodoPane() = Grid {
    css {
        position = Position.absolute
        left = 100.px
        top = 100.px
        width = 400.px
        padding(0.5.em)
        boxShadow(Color.gray, blurRadius = 2.px, spreadRadius = 1.px)
    }
    repeat(5) {
        Todo(null)
    }
    PostEditor(onSubmit = {})
}