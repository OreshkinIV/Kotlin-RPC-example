package org.example.krpc.models.responses

import io.ktor.http.HttpStatusCode

class ErrorResponse(
    val httpStatusCode: HttpStatusCode,
    message: String,
) : Exception(message)