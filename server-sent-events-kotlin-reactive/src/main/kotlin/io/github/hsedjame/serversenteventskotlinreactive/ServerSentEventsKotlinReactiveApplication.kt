package io.github.hsedjame.serversenteventskotlinreactive

import io.github.hsedjame.serversenteventskotlinreactive.models.ChatEvent
import io.github.hsedjame.serversenteventskotlinreactive.models.ChatState
import io.github.hsedjame.serversenteventskotlinreactive.models.MsgRequest
import io.github.hsedjame.serversenteventskotlinreactive.services.ChatService
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Sinks
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference
import kotlin.concurrent.timer


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