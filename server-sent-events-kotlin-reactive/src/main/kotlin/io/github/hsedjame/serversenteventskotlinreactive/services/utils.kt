package io.github.hsedjame.serversenteventskotlinreactive.services

import kotlinx.coroutines.runBlocking
import kotlin.concurrent.timer

fun <T> withDelay(delay: Long, t: T, action: suspend T.() -> Unit) {
    timer("action-timer", false, delay, 1) {
        runBlocking {
            action.invoke(t)
        }

        cancel()
    }
}