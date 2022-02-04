package io.github.hsedjame.serversenteventskotlinreactive

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.selects.select
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyExtractor
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.netty.http.server.HttpServerRequest
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentSkipListMap
import java.util.concurrent.ConcurrentSkipListSet
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask


@SpringBootApplication
class ServerSentEventsKotlinReactiveApplication {


    @Bean
    fun chatEventSink() = Sinks.many().replay().all<ChatEvent>()

    @Bean
    fun chatState() = ChatState(
        participants = ConcurrentSkipListSet(),
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

                            withDelay(2000, emitter) {
                                newParticipant(name)
                            }
                            ServerResponse.ok().buildAndAwait()
                        }
                }

                POST("/{name}"){ it ->

                   it.awaitBody<MsgRequest>().let { msg->
                       withDelay(1000, emitter){
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