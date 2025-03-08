package org.example.krpc

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.example.krpc.presentation.auth.AuthScreen
import org.example.krpc.presentation.routes.Route
import org.example.krpc.presentation.home.HomeScreen
import org.example.krpc.presentation.splash.SplashScreen

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.Splash) {
        composable<Route.Splash> {
            SplashScreen(navController = navController)
        }
        composable<Route.Auth> {
            AuthScreen(navController = navController)
        }
        composable<Route.Home> { backStackEntry ->
            val home: Route.Home = backStackEntry.toRoute()
            val from = home.from

            HomeScreen(navController = navController, from)
        }
    }
}
