package io.github.hsedjame.serversenteventskotlinreactive

import io.github.hsedjame.serversenteventskotlinreactive.models.ChatEvent
import io.github.hsedjame.serversenteventskotlinreactive.models.ChatState
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import reactor.core.publisher.Sinks
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference


@SpringBootApplication
class ServerSentEventsKotlinReactiveApplication {


    @Bean
    fun chatEventSink() = Sinks.many().replay().all<ChatEvent>()

    @Bean
    fun chatState() = ChatState(
        participants = AtomicReference(TreeSet()),
        messages = ConcurrentHashMap()
    )
}


fun main(args: Array<String>) {
    runApplication<ServerSentEventsKotlinReactiveApplication>(*args)
}