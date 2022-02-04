package io.github.hsedjame.serversenteventskotlinreactive

import org.springframework.stereotype.Service
import reactor.core.publisher.SignalType
import reactor.core.publisher.Sinks
import java.time.LocalDateTime

@Service
data class EventEmitter(private val chatSink: Sinks.Many<ChatEvent>, private val state: ChatState) {

    suspend fun newParticipant(name: String) {

        when(!state.participants.map { it.name }.contains(name)) {
            true -> {
                state.participants.add(Participant(name))
                state.messages[name] = ArrayList()
                emitEvent(NewParticipant(name), name, "")
            }

            false -> {
                chatSink.tryEmitNext(NewError(name, "This username is already used"))
            }
        }

    }

    suspend fun newMessage(sender: String, message: String) {

        when (state.messages.contains(sender) ) {
            true -> {
                state.messages[sender].let {
                    it?.add(Message(message, LocalDateTime.now()))
                }
                emitEvent(NewMessage(sender, message), sender, "Oups an error occured")
            }
            false -> {

            }
        }

    }

    private fun emitEvent(event: ChatEvent, author: String, errorMsg: String) {
        chatSink.emitNext(event) { type, result ->
            when (type) {
                SignalType.ON_ERROR -> {
                    chatSink.tryEmitNext(NewError(author, errorMsg))
                }
                else -> {}
            }
            true
        }
    }
}