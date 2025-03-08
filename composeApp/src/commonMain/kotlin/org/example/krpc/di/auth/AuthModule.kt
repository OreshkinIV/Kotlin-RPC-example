package org.example.krpc.di.auth

import org.example.krpc.data.api.rpc.repo.AuthRepositoryImpl
import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.domain.usecases.auth.rest.RestRegisterUserUseCase
import org.example.krpc.domain.usecases.auth.rest.RestRegisterUserUseCaseImpl
import org.example.krpc.domain.usecases.auth.rpc.LoginUserUseCase
import org.example.krpc.domain.usecases.auth.rpc.LoginUserUseCaseImpl
import org.example.krpc.domain.usecases.auth.rpc.RefreshTokenUseCase
import org.example.krpc.domain.usecases.auth.rpc.RefreshTokenUseCaseImpl
import org.example.krpc.domain.usecases.auth.rpc.RegisterUserUseCase
import org.example.krpc.domain.usecases.auth.rpc.RegisterUserUseCaseImpl
import org.koin.dsl.module

val authModule
    get() = module {
        single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
        single<RegisterUserUseCase> { RegisterUserUseCaseImpl(get()) }
        single<RestRegisterUserUseCase> { RestRegisterUserUseCaseImpl(get(), get()) }
        single<LoginUserUseCase> { LoginUserUseCaseImpl(get()) }
        single<RefreshTokenUseCase> { RefreshTokenUseCaseImpl(get()) }
    }