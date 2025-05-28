package org.example.krpc.di.user

import org.example.krpc.data.api.rpc.repo.MessagesRepositoryImpl
import org.example.krpc.data.api.rpc.repo.UserRepositoryImpl
import org.example.krpc.domain.repo.MessagesRepository
import org.example.krpc.domain.repo.UserRepository
import org.example.krpc.domain.usecases.messages.ListenMessagesUseCase
import org.example.krpc.domain.usecases.messages.ListenMessagesUseCaseImpl
import org.example.krpc.domain.usecases.user.GetUserJwtPayloadUseCase
import org.example.krpc.domain.usecases.user.GetUserJwtPayloadUseCaseImpl
import org.example.krpc.domain.usecases.user.LoadFileUseCase
import org.example.krpc.domain.usecases.user.LoadFileUseCaseImpl
import org.example.krpc.domain.usecases.user.LoadFileWithProgressUseCase
import org.example.krpc.domain.usecases.user.LoadFileWithProgressUseCaseImpl
import org.example.krpc.domain.usecases.messages.SendMessageUseCase
import org.example.krpc.domain.usecases.messages.SendMessageUseCaseImpl
import org.koin.dsl.module

val userModule
    get() = module {
        single<UserRepository> { UserRepositoryImpl(get()) }
        single<MessagesRepository> { MessagesRepositoryImpl(get()) }
        single<GetUserJwtPayloadUseCase> { GetUserJwtPayloadUseCaseImpl(get()) }
        single<ListenMessagesUseCase> { ListenMessagesUseCaseImpl(get()) }
        single<LoadFileUseCase> { LoadFileUseCaseImpl(get()) }
        single<LoadFileWithProgressUseCase> { LoadFileWithProgressUseCaseImpl(get()) }
        single<SendMessageUseCase> { SendMessageUseCaseImpl(get()) }
    }