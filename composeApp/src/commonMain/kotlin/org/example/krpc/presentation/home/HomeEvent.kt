package org.example.krpc.presentation.home

import org.example.krpc.presentation.routes.Route

sealed class HomeEvent {

    data class Logout(val destination: Route.Auth = Route.Auth): HomeEvent()

    data class ShowSnackbar(val message: String): HomeEvent()

    data class UpdateProgress(val message: String): HomeEvent()
}