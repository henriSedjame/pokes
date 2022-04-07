package io.github.hsedjame

import io.github.hsedjame.ktor.grpc.server.GreeterGrpc
import io.github.hsedjame.ktor.grpc.server.GreeterGrpc.GreeterStub
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.github.hsedjame.plugins.*
import io.grpc.ManagedChannelBuilder

fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0") {

        val chan = ManagedChannelBuilder
            .forAddress("localhost", 7777)
            .usePlaintext()
            .build()

        val greeter = GreeterGrpc.newStub(chan)

        configureRouting(HelloClient(greeter))
        configureSerialization()
    }.start(wait = true)
}
