package org.example.krpc.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class JwtPayload(
    val expiresAt: Long?,
    val issuer: String?,
    val audience: List<String>?
)