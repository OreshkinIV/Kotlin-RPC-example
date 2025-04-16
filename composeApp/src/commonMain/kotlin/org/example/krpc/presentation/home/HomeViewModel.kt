package org.example.krpc.presentation.home

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.krpc.data.network.InvalidRefreshTokenException
import org.example.krpc.data.preferences.PreferencesKeys
import org.example.krpc.domain.usecases.user.ListenMessagesUseCase
import org.example.krpc.domain.usecases.user.GetUserJwtPayloadUseCase
import org.example.krpc.domain.usecases.user.LoadFileUseCase
import org.example.krpc.domain.usecases.user.SendMessageUseCase
import org.example.krpc.presentation.base.ext.BaseViewModel

class HomeViewModel(
    private val dataStore: DataStore<Preferences>,
    private val getUserJwtPayloadUseCase: GetUserJwtPayloadUseCase,
    private val listenMessagesUseCase: ListenMessagesUseCase,
    private val loadFileUseCase: LoadFileUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
) : BaseViewModel<HomeEvent>() {

    init {
        listenMessages()
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTH_TOKEN] = ""
                _events.emit(HomeEvent.Logout())
            }
        }
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            runCatching {
                sendMessageUseCase(message)
            }.fold(
                onSuccess = {
                },
                onFailure = {
                    emitErrorEvent(it)
                }
            )
        }
    }

    private fun listenMessages() {
        viewModelScope.launch {
            runCatching {
                listenMessagesUseCase().catch {
                    emitErrorEvent(it)
                }.collect { value ->
                    _events.emit(HomeEvent.ShowSnackbar(value))
                }
            }.fold(
                onSuccess = {
                },
                onFailure = {
                    emitErrorEvent(it)
                }
            )
        }
    }

    fun loadFile(byteArray: ByteArray, name: String) {
        viewModelScope.launch {
            runCatching {
                loadFileUseCase(byteArray, name)
            }.fold(
                onSuccess = {
                    _events.emit(HomeEvent.ShowSnackbar("Success"))
                },
                onFailure = {
                    emitErrorEvent(it)
                }
            )
        }
    }

    fun getUserJwtPayload() {
        viewModelScope.launch {
            kotlin.runCatching {
                getUserJwtPayloadUseCase()
            }.fold(
                onSuccess = {
                    _events.emit(HomeEvent.ShowSnackbar(it.toString()))
                },
                onFailure = {
                    emitErrorEvent(it)
                }
            )
        }
    }

    private suspend fun emitErrorEvent(t: Throwable) {
        if (t is InvalidRefreshTokenException) {
            _events.emit(HomeEvent.Logout())
        } else {
            _events.emit(HomeEvent.ShowSnackbar(t.message.orEmpty()))
        }
    }
}
