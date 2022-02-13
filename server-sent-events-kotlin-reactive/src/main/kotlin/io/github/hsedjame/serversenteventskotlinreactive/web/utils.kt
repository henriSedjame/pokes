package io.github.hsedjame.serversenteventskotlinreactive.web

import kotlinx.coroutines.runBlocking
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait
import kotlin.concurrent.thread

fun runAsync(fn : suspend () -> Unit) {
    thread(start = true, isDaemon = false) {
        runBlocking {
            fn()
        }
    }
}


suspend fun handleRequest(request: ServerRequest, fn : suspend (String) -> Unit) : ServerResponse {
    runAsync {
         fn(request.pathVariable("name"))
    }
    return ServerResponse.ok().buildAndAwait()
}