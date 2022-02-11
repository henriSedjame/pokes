package io.github.hsedjame.serversenteventskotlinreactive

import org.springframework.stereotype.Service
import reactor.core.publisher.SignalType
import reactor.core.publisher.Sinks
import java.time.LocalDateTime

@Service
data class EventEmitter(private val chatSink: Sinks.Many<ChatEvent>, private val state: ChatState) {

    suspend fun newParticipant(name: String) {

        when(!state.participants.get().map { it.name }.contains(name)) {
            true -> {
                state.participants.get().add(Participant(name))
                state.messages[name] = ArrayList()
                emitEvent(NewParticipant(name), name, "")
            }

            false -> {
                chatSink.tryEmitNext(NewError(name, "This username is already used"))
            }
        }

    }

    suspend fun newMessage(sender: String, message: String) {

        when (state.participants.get().map { it.name }.contains(sender)) {
            true -> {
                if (isCorrect(message)) {
                    state.messages[sender].let {
                        it?.add(Message(message, LocalDateTime.now()))
                    }
                    emitEvent(NewMessage(sender, message), sender, "Oups an error occured")
                } else {
                    chatSink.tryEmitNext(ModeratorMessage(sender, "Your message is not correct and has been censored by moderator."))
                }
            }
            false -> {
                chatSink.tryEmitNext(NewError(sender, "This username is already used"))
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

    private fun isCorrect(message: String): Boolean {
        return !message.contains(Regex("putain|merde|salope", RegexOption.IGNORE_CASE))
    }
}

