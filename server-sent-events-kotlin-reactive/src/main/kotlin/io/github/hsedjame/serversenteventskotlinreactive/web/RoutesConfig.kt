package io.github.hsedjame.serversenteventskotlinreactive.web

import io.github.hsedjame.serversenteventskotlinreactive.models.ChatEvent
import io.github.hsedjame.serversenteventskotlinreactive.models.ChatState
import io.github.hsedjame.serversenteventskotlinreactive.models.MsgRequest
import io.github.hsedjame.serversenteventskotlinreactive.services.ChatService
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Sinks
import kotlin.concurrent.thread

@Configuration
class RoutesConfig {

    fun runAsync(fn : suspend () -> Unit) {
        thread(start = true, isDaemon = false) {
            runBlocking {
                fn()
            }
        }
    }


    suspend fun handleRequest(request: ServerRequest, fn : suspend () -> Unit) : ServerResponse {
        runAsync {
           fn()
        }

        return ServerResponse.ok().buildAndAwait()
    }

    @Bean
    fun routes(
        state: ChatState,
        sink: Sinks.Many<ChatEvent>,
        service: ChatService,
        @Value("classpath:/static/index.html") html: Resource
    ) = coRouter {

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
                    handleRequest(it) {
                        service.onNewUser(it.pathVariable("name"))
                    }
                }

                POST("/{name}") { it ->
                    handleRequest(it) {
                        service.onNewMessage(it.pathVariable("name"), it.awaitBody<MsgRequest>().message)
                    }
                }
            }
        }
    }
}