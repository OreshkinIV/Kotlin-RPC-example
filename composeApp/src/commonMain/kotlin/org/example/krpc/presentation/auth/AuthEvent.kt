package org.example.krpc.presentation.auth

import org.example.krpc.presentation.routes.Route

sealed class AuthEvent {
    data class NavigateToHome(val destination: Route.Home = Route.Home("AuthScreen")) : AuthEvent()
    data class ShowSnackbar(val message: String): AuthEvent()
}