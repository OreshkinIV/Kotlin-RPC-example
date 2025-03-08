package org.example.krpc.presentation.auth

import androidx.lifecycle.viewModelScope
import org.example.krpc.models.requests.AuthBody
import kotlinx.coroutines.launch
import org.example.krpc.domain.usecases.auth.rest.RestRegisterUserUseCase
import org.example.krpc.domain.usecases.auth.rpc.LoginUserUseCase
import org.example.krpc.domain.usecases.auth.rpc.RegisterUserUseCase
import org.example.krpc.presentation.base.ext.BaseViewModel

class AuthViewModel(
    private val registerUserUseCase: RegisterUserUseCase,
    private val restRegisterUserUseCase: RestRegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
) : BaseViewModel<AuthEvent>() {

    fun register(
        login: String,
        password: String,
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                /** rest api example */
//                restRegisterUserUseCase(AuthBody(login, password))
                /** rpc example */
                registerUserUseCase(AuthBody(login, password))
            }.fold(
                onSuccess = {
                    _events.emit(AuthEvent.NavigateToHome())
                },
                onFailure = {
                    _events.emit(AuthEvent.ShowSnackbar(it.message.orEmpty()))
                })
        }
    }

    fun login(
        login: String,
        password: String,
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                loginUserUseCase(AuthBody(login, password))
            }.fold(
                onSuccess = {
                    _events.emit(AuthEvent.NavigateToHome())
                },
                onFailure = {
                    _events.emit(AuthEvent.ShowSnackbar(it.message.orEmpty()))
                })
        }
    }
}
