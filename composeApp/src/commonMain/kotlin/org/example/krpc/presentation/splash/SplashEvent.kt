package org.example.krpc.presentation.splash

import org.example.krpc.presentation.routes.Route

sealed class SplashEvent {
    data class NavigateToAuth(val destination: Route.Auth = Route.Auth) : SplashEvent()
    data class NavigateToHome(val destination: Route.Home = Route.Home("SplashScreen")) : SplashEvent()
    data class ShowSnackbar(val message: String): SplashEvent()
}