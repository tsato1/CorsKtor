package org.example

import react.FC
import react.Props
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.dom.html.ReactHTML.button
import web.prompts.alert

private val scope = MainScope()

val App = FC<Props> { props ->
    button {
        onClick = {
            scope.launch {
                console.log("button clicked!")
                val response = saveCustomer()
                alert("${response.status.value} ${response.status.description}")
            }
        }
        +"Make a CORS request to save a customer"
    }
}