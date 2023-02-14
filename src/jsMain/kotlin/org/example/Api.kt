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

suspend fun getCustomer(id: String): HttpResponse {
    return jsonClient.get("http://0.0.0.0:9090/customer/$id") {
        header(HttpHeaders.Accept, ContentType.Application.OctetStream.toString())
    }
}

suspend fun saveCustomer(): HttpResponse {
    return jsonClient.post("http://0.0.0.0:9090/customer") {
        header(HttpHeaders.ContentType, "application/json")
        setBody(Customer(id = 3, firstName = "Jet", lastName = "Brains"))
    }
}

suspend fun getCustomers(): HttpResponse {
    return jsonClient.get("http://0.0.0.0:9090/customers") {
        header(HttpHeaders.Accept, ContentType.Application.OctetStream.toString())
    }
}