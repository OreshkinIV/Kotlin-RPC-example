package org.example.krpc.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.sample.library.resources.Res
import me.sample.library.resources.auth_title
import me.sample.library.resources.login
import me.sample.library.resources.password
import me.sample.library.resources.register
import me.sample.library.resources.sign_in
import org.example.krpc.presentation.base.ext.observeWithLifecycle
import org.example.krpc.presentation.routes.Route
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    navController: NavHostController,
    viewModel: AuthViewModel = koinViewModel<AuthViewModel>()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    viewModel.events.observeWithLifecycle { event ->
        when (event) {
            is AuthEvent.NavigateToHome -> {
                navController.navigate(event.destination) {
                    popUpTo(Route.Auth) { inclusive = true }
                }
            }

            is AuthEvent.ShowSnackbar -> {
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val username = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }
            val password = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

            Text(
                text = stringResource(Res.string.auth_title),
                style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.login)) },
                value = username.value,
                onValueChange = { username.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.password)) },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it })

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.login(username.value.text, password.value.text)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.sign_in))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.register(username.value.text, password.value.text)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.register))
            }
        }
    }
}