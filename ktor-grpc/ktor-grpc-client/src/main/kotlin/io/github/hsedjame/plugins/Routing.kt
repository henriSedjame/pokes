package io.github.hsedjame.plugins

import io.github.hsedjame.HelloClient
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting(client: HelloClient) {

    routing {
        get("/") {

            call.respondText("Hello")
        }
        get("/hello/{name}") {
            val name = call.parameters["name"]!!
            client.greet(name)
            call.respond("Ok, message received")
        }
    }
}
