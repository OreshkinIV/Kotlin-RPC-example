package ktor.backend.plugins.statuspages

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.StatusPages
import org.example.krpc.models.responses.ErrorResponse
import io.ktor.server.response.*

fun Application.installStatusPages() {
    install(StatusPages) {
        exception<ErrorResponse> { call, error ->
            call.respond(error.httpStatusCode, error)
        }
        exception<Throwable> { call, _ ->
            call.respond(
                HttpStatusCode.InternalServerError, ErrorResponse(HttpStatusCode.InternalServerError, "Unknown error")
            )
        }
    }
}