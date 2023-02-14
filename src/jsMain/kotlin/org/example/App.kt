package org.example

import io.ktor.client.statement.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import web.prompts.alert

private val scope = MainScope()

val App = FC<Props> { props ->
    button {
        onClick = {
            scope.launch {
                console.log("GET button clicked!")
                val response = getCustomer("1")
                alert("${response.status.value} ${response.bodyAsText()}")
            }
        }
        +"Get a customer with id=1"
    }
    button {
        onClick = {
            scope.launch {
                console.log("SAVE button clicked!")
                val response = saveCustomer()
                alert("${response.status.value} ${response.status.description}")
            }
        }
        +"Save a customer"
    }
    button {
        onClick = {
            scope.launch {
                console.log("Get List button clicked!")
                val response = getCustomers()
                alert("${response.status.value} ${response.bodyAsText()}")
            }
        }
        +"Get a customer list"
    }
}