package org.example.krpc.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val authToken: String? = null,
    val refreshToken: String? = null
)
