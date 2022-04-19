package com.action

import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketSession, val name: String) {
    companion object {
        var lastId = AtomicInteger(0)
    }
}