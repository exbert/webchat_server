package com.action

import io.ktor.server.application.*
import com.action.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.tomcat.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureRouting()
    configureSockets()
}
