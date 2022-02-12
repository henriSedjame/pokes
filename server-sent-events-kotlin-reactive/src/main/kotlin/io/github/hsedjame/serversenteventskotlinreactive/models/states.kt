package io.github.hsedjame.serversenteventskotlinreactive.models

import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

data class ChatState(
    val participants: AtomicReference<TreeSet<Participant>>,
    val messages: ConcurrentHashMap<String, ArrayList<Message>>)


@JvmInline
value class Participant(val name: String): Comparable<Participant> {
    override fun compareTo(other: Participant): Int = this.name.compareTo(other.name)
}

data class Message(val value: String, val date: LocalDateTime)

