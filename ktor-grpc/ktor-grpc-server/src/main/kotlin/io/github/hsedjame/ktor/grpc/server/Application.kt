package io.github.hsedjame.ktor.grpc.server

import io.github.hsedjame.ktor.grpc.server.grpc.GrpcServer
import io.github.hsedjame.ktor.grpc.server.plugins.configureRouting
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlin.concurrent.thread


fun main(args: Array<String>): Unit {

    thread(true) {
        embeddedServer(GrpcServer, configure = {
            port = 7777
            serverConfigurer = {
                this.addService(HelloService())
            }
        }) {}.start(true)
    }

    thread(true) {
        embeddedServer(Netty, port = 8080, host = "0.0.0.0"){
            configureRouting()
        }.start(true)
    }


}
