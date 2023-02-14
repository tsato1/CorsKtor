package org.example

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import java.io.File

fun HTML.index() {
    head {
        meta {
            charset = "UTF-8"
        }
        title {
            +"Cors Test"
        }
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/Cors.js") {}
    }
}

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
                call.respondHtml(HttpStatusCode.OK, HTML::index)
            }
            static("/static") {
                staticRootFolder = File("assets")
                files("js")
                resources()
            }
        }
    }
}

fun Application.backend() {
    val customerStorage = mutableListOf<Customer>(
        Customer(id = 0, firstName = "Taro", lastName = "Brains"),
        Customer(id = 1, firstName = "Momoko", lastName = "Adams"),
        Customer(id = 2, firstName = "Jet", lastName = "Brains")
    )

    /* cors not necessary in backend if reverse proxy is prepared (in which cors is implemented) */
//    install(CORS) {
//        allowHost("0.0.0.0:3000")
//        allowHeader(HttpHeaders.ContentType)
//        allowMethod(HttpMethod.Post)
//    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        port(8080) {
            get("/customer/{id}") {
                val id = call.parameters["id"]?.toInt() ?: -1
                if (id == -1 || customerStorage.size <= id) {
                    call.respond(HttpStatusCode.NotFound, "Noooooo!")
                }
                else {
                    call.respond(customerStorage[id])
                }
            }
            get("/customers") {
                call.respond(customerStorage.toList())
            }
            post("/customer") {
                val customer = call.receive<Customer>()
                customerStorage.add(customer)
                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }
        }
    }
}