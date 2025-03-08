package org.example.krpc.domain.usecases.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.domain.repo.UserRepository

interface SendMessageUseCase {
    suspend operator fun invoke(message: String)
}

class SendMessageUseCaseImpl(
    private val userRepository: UserRepository,
) : SendMessageUseCase {

    override suspend fun invoke(message: String) {
        return withContext(Dispatchers.IO) {
            userRepository.sendMessage(message)
        }
    }
}
