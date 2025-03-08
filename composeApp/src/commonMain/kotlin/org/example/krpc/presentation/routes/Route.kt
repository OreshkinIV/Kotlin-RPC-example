package org.example.krpc.presentation.routes

import kotlinx.serialization.Serializable

sealed class Route {
    @Serializable
    data object Splash: Route()

    @Serializable
    data object Auth : Route()

    @Serializable
    data class Home(
        val from: String
    ): Route()
}
