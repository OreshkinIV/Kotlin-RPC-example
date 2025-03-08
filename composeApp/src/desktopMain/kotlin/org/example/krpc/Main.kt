package org.example.krpc

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.krpc.di.initKoin

/** .\gradlew :composeApp:run */
fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "kotlin-rpc-example",
    ) {
        App()
    }
}