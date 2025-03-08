package org.example.krpc.presentation.splash

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.example.krpc.data.preferences.getToken
import org.example.krpc.presentation.base.ext.BaseViewModel
import org.example.krpc.presentation.routes.Route

class SplashViewModel(
    private val dataStore: DataStore<Preferences>,
) : BaseViewModel<SplashEvent>() {

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch {
            val token = dataStore.getToken()
            if (token.isNotBlank()) {
                _events.emit(SplashEvent.NavigateToHome(Route.Home("SplashScreen")))
            } else {
                _events.emit(SplashEvent.NavigateToAuth(Route.Auth))
            }
        }
    }
}
