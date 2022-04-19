package com.action.plugins

import com.action.Connection
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        var lastId = AtomicInteger(0)
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/chat") {
            send("Enter your username: ")
            val userName = (incoming.receive() as Frame.Text).readText().ifEmpty { "user${lastId.getAndIncrement()}" }
            println("Adding user!")
            val thisConnection = Connection(this, userName)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                send("Room commands are #list, #help .")

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    when (receivedText) {
                        "#list" -> {
                            send("List of chat Rooms: ")
                        }
                        "#help" -> {
                            send("Help commands.")
                        }
                    }
                    val textWithUsername = "[${thisConnection.name}]: $receivedText"
                    connections.forEach {
                        it.session.send(textWithUsername)
                    }
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing $thisConnection")
                connections -= thisConnection
            }
        }
    }
}
