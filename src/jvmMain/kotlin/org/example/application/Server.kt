package org.example.application

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    val env = applicationEngineEnvironment {
        module {
            backend()
            frontend()
        }
        connector {
            host = "0.0.0.0"
            port = 8080
        }
        connector {
            host = "0.0.0.0"
            port = 3000
        }
    }
    embeddedServer(Netty, env).start(wait = true)
}

fun Application.frontend() {
    routing {
        port(3000) {
            get("/") {
                call.respondHtml {
                    head {
                        script(src = "/static/script.js") {}
                    }
                    body {
                        button {
                            onClick = "saveCustomer()"
                            + "Make a CORS request to save a customer"
                        }
                    }
                }
            }
            static("/static") {
                staticRootFolder = File("assets")
                files("js")
            }
        }
    }
}

@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)

fun Application.backend() {
    val customerStorage = mutableListOf<Customer>()
    install(CORS) {
        allowHost("0.0.0.0:3000")
        allowHeader(HttpHeaders.ContentType)
    }
    install(ContentNegotiation) {
        json(Json)
    }
    routing {
        port(8080) {
            post("/customer") {
                val customer = call.receive<Customer>()
                customerStorage.add(customer)
                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }
        }
    }
}