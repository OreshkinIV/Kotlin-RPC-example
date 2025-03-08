package org.example.krpc.models.responses

import kotlinx.serialization.Serializable

@Serializable
class ErrorResponse(
    val message: String,
    val code: Int = -1,
)