package ktor.backend.ktor.backend.plugins.serialization

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun Application.installSerialization() {
    install(ContentNegotiation) {
        Json {
            ignoreUnknownKeys = true
        }
    }
}