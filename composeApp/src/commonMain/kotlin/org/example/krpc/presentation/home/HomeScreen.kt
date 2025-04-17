package org.example.krpc.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import me.sample.library.resources.Res
import me.sample.library.resources.from_screen
import me.sample.library.resources.get_user_jwt
import me.sample.library.resources.image
import me.sample.library.resources.load_file
import me.sample.library.resources.load_file_with_progress
import me.sample.library.resources.logout
import me.sample.library.resources.message
import me.sample.library.resources.send_message
import org.example.krpc.presentation.base.ext.observeWithLifecycle
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.rememberResourceEnvironment
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    from: String,
    viewModel: HomeViewModel = koinViewModel<HomeViewModel>()
) {
    var fileProgress by remember { mutableStateOf("progress: 0") }

    val resEnv = rememberResourceEnvironment()
    var file by remember { mutableStateOf(ByteArray(0)) }
    LaunchedEffect(Unit) {
        file = getDrawableResourceBytes(resEnv, Res.drawable.image)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val message = rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue()) }

    viewModel.events.observeWithLifecycle { event ->
        when (event) {
            is HomeEvent.Logout -> {
                navController.navigate(event.destination) {
                    navController.currentDestination?.route?.let { popUpTo(it) { inclusive = true } }
                }
            }

            is HomeEvent.ShowSnackbar -> {
                snackbarHostState.showSnackbar(
                    message = event.message
                )
            }

            is HomeEvent.UpdateProgress -> {
                fileProgress = event.message
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
            /** from */
            Text(
                text = stringResource(Res.string.from_screen, from),
                style = TextStyle(fontSize = 24.sp, fontFamily = FontFamily.SansSerif)
            )

            Spacer(modifier = Modifier.height(20.dp))

            /** Получить jwt пользователя */
            Button(
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.getUserJwtPayload()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.get_user_jwt))
            }

            Spacer(modifier = Modifier.height(20.dp))

            /** Выйти */
            Button(
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.logout()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.logout))
            }

            Spacer(modifier = Modifier.height(50.dp))

            /** сообщение */
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(Res.string.message)) },
                value = message.value,
                onValueChange = { message.value = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            /** отправить сообщение */
            Button(
                onClick = {
                    viewModel.sendMessage(message.value.text)
                    message.value = TextFieldValue()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.send_message))
            }

            Spacer(modifier = Modifier.height(20.dp))

            /** отправить файл */
            Button(
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.loadFile(file, "image.jpeg")
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.load_file))
            }

            Spacer(modifier = Modifier.height(20.dp))

            /** отправить файл с прогрессом */
            Button(
                onClick = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    viewModel.loadFileWithProgress(file, "image.jpeg")
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = stringResource(Res.string.load_file_with_progress))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = fileProgress)
        }
    }
}