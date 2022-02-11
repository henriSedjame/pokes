package io.github.hsedjame.serversenteventskotlinreactive

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

    fun withDelay(delay: Long, eventEmitter: EventEmitter,  action: suspend EventEmitter.() -> Unit) {
        timer("action-timer", false, delay, 1) {
            runBlocking {
                action.invoke(eventEmitter)
            }

            cancel()
        }
    }

    @Bean
    fun routes(state: ChatState, sink: Sinks.Many<ChatEvent>, emitter: EventEmitter, @Value("classpath:index.html")  html: Resource) = coRouter {

        GET("") {
            ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValueAndAwait(html)
        }

        "/chat".nest {
            GET("/events") {
                ServerResponse
                    .ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .bodyAndAwait(sink.asFlux().asFlow())
            }


            accept(MediaType.APPLICATION_JSON).nest {

                GET("/{name}") {
                    it.pathVariable("name")
                        .let { name ->

                            withDelay(10, emitter) {
                                newParticipant(name)
                            }
                            ServerResponse.ok().buildAndAwait()
                        }
                }

                POST("/{name}"){ it ->

                   it.awaitBody<MsgRequest>().let { msg->
                       withDelay(10, emitter){
                           newMessage(it.pathVariable("name"), msg.message)
                       }
                   }

                    ServerResponse.ok().buildAndAwait()


                }
            }

        }
    }
}


fun main(args: Array<String>) {
    runApplication<ServerSentEventsKotlinReactiveApplication>(*args)
}