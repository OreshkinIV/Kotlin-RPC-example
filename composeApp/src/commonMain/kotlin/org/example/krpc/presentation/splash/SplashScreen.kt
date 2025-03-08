package org.example.krpc.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.sample.library.resources.Res
import me.sample.library.resources.rpc
import org.example.krpc.presentation.base.ext.observeWithLifecycle
import org.example.krpc.presentation.routes.Route
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = koinViewModel<SplashViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.events.observeWithLifecycle { event ->
        when (event) {
            is SplashEvent.NavigateToHome -> {
                navController.navigate(event.destination) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            }

            is SplashEvent.NavigateToAuth -> {
                navController.navigate(event.destination) {
                    popUpTo(Route.Splash) { inclusive = true }
                }
            }

            is SplashEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }) {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Res.drawable.rpc), "logo", modifier = Modifier
                    .width(120.dp)
                    .height(120.dp)
            )
        }
    }
}