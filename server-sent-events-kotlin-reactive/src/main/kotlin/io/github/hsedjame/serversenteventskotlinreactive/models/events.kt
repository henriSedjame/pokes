package io.github.hsedjame.serversenteventskotlinreactive.models


enum class EventType {
    NEW_PARTICIPANT,
    NEW_MESSAGE,
    MODERATOR_MESSAGE,
    ERROR
}

sealed class ChatEvent(val type: EventType)

data class NewParticipant(val name: String) : ChatEvent(EventType.NEW_PARTICIPANT)

data class NewMessage(val author: String, val message: String): ChatEvent(EventType.NEW_MESSAGE)

data class ModeratorMessage(val author: String, val message: String): ChatEvent(EventType.MODERATOR_MESSAGE)

data class NewError(val receiver: String, val message: String) : ChatEvent(EventType.ERROR)