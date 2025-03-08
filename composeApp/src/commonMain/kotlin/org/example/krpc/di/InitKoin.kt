package org.example.krpc.di

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin

fun initKoin(){
    startKoin {
        modules(appModules)
    }
}

inline fun <reified T: Any> inject(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}
