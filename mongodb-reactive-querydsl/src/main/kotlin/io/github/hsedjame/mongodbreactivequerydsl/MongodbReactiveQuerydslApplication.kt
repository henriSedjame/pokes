package io.github.hsedjame.mongodbreactivequerydsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MongodbReactiveQuerydslApplication

fun main(args: Array<String>) {
    runApplication<MongodbReactiveQuerydslApplication>(*args)
}
