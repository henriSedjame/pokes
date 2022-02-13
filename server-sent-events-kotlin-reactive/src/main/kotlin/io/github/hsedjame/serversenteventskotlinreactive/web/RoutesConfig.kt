package io.github.hsedjame.serversenteventskotlinreactive.web

import io.github.hsedjame.serversenteventskotlinreactive.models.ChatEvent
import io.github.hsedjame.serversenteventskotlinreactive.models.ChatState
import io.github.hsedjame.serversenteventskotlinreactive.models.MsgRequest
import io.github.hsedjame.serversenteventskotlinreactive.services.ChatService
import kotlinx.coroutines.reactive.asFlow
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Sinks

@Configuration
class RoutesConfig {


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
                        name -> service.onNewUser(name)
                    }
                }

                POST("/{name}") { it ->
                    handleRequest(it) {
                        name -> service.onNewMessage(name, it.awaitBody<MsgRequest>().message)
                    }
                }
            }
        }
    }
}