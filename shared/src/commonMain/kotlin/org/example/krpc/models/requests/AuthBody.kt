package org.example.krpc.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthBody(
    val login: String,
    val password: String
)
