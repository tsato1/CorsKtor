package org.example

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

suspend fun saveCustomer(): HttpResponse {
    val response = jsonClient.post("http://0.0.0.0:8080/customer") {
        header(HttpHeaders.Accept, "application/json")
        header(HttpHeaders.ContentType, "application/json")
        setBody(Customer(id = 3, firstName = "Jet", lastName = "Brains"))
    }
    return response
}