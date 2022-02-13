package io.github.hsedjame.serversenteventskotlinreactive.services

interface ChatService {

    suspend fun onNewUser(name: String)

    suspend fun onNewMessage(sender: String, message: String)
}