package io.github.hsedjame.serversenteventskotlinreactive.services

import io.github.hsedjame.serversenteventskotlinreactive.models.*
import org.springframework.stereotype.Service
import reactor.core.publisher.SignalType
import reactor.core.publisher.Sinks
import java.time.LocalDateTime

@Service
data class ChatService(private val chatSink: Sinks.Many<ChatEvent>, private val state: ChatState) {

    suspend fun newParticipant(name: String) {

        when(!state.participants.get().map { it.name }.contains(name)) {
            true -> {
                val element = Participant(name)
                state.participants.get().add(element)
                state.messages[name] = ArrayList()

                emitEvent(NewParticipant(name), name, "Oups!! an error occurred 🙁") {
                    state.participants.get().remove(element)
                    state.messages.remove(name)
                }
            }

            false -> {
                chatSink.tryEmitNext(NewError(name, "This username is already used, please choose another one."))
            }
        }

    }

    suspend fun newMessage(sender: String, message: String) {

        when (state.participants.get().map { it.name }.contains(sender)) {
            true -> {
                if (isCorrect(message)) {
                    val element = Message(message, LocalDateTime.now())

                    state.messages[sender].let {
                        it?.add(element)
                    }

                    emitEvent(NewMessage(sender, message), sender, "Oups an error occured"){
                        state.messages[sender].let {
                            it?.remove(element)
                        }
                    }

                } else {
                    chatSink.tryEmitNext(ModeratorMessage(sender, "Your message is not correct and has been censored by moderator."))
                }
            }
            false -> {
                chatSink.tryEmitNext(NewError(sender, "This username is already used"))
            }
        }

    }

    private fun emitEvent(event: ChatEvent, author: String, errorMsg: String, reverse: () -> Unit) {

        chatSink.emitNext(event) { type, _ ->
            when (type) {
                SignalType.ON_ERROR -> {
                    chatSink.tryEmitNext(NewError(author, errorMsg))
                    reverse()
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

