package io.github.hsedjame.ktor.grpc.server

import io.github.hsedjame.ktor.grpc.server.grpc.GrpcServer
import io.github.hsedjame.ktor.grpc.server.plugins.configureRouting
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun main(args: Array<String>): Unit = coroutineScope {

    launch {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0"){
            configureRouting()
        }.start(true)
    }

    launch {
        embeddedServer(GrpcServer) {}.start(true)
    }
}
