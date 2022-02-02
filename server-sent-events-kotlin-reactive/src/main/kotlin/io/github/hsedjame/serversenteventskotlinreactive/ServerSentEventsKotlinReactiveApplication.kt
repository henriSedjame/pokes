package io.github.hsedjame.serversenteventskotlinreactive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerSentEventsKotlinReactiveApplication

fun main(args: Array<String>) {
    runApplication<ServerSentEventsKotlinReactiveApplication>(*args)
}
