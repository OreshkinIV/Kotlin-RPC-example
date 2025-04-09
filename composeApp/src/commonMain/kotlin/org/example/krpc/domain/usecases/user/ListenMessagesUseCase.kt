package org.example.krpc.domain.usecases.user

import kotlinx.coroutines.flow.Flow
import org.example.krpc.domain.repo.UserRepository

interface ListenMessagesUseCase {
    suspend operator fun invoke(): Flow<String>
}

class ListenMessagesUseCaseImpl(
    private val userRepository: UserRepository,
) : ListenMessagesUseCase {

    override suspend fun invoke(): Flow<String> {
        return userRepository.listenMessages()
    }
}
