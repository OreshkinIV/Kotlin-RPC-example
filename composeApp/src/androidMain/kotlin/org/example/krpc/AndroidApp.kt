package org.example.krpc

import android.app.Application
import org.example.krpc.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModules)
            androidContext(this@AndroidApp)
        }
    }
}