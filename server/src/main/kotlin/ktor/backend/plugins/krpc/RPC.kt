package ktor.backend.plugins.krpc

import io.ktor.server.application.*
import kotlinx.rpc.krpc.ktor.server.Krpc
import kotlinx.rpc.krpc.serialization.json.json

fun Application.installKRPC() {
    install(Krpc) {
        serialization {
            json()
        }
        waitForServices = true
    }
}
