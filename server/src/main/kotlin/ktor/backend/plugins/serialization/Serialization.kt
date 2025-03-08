package ktor.backend.ktor.backend.plugins.serialization

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

fun Application.installSerialization() {
    install(ContentNegotiation) {
        json()
    }
}