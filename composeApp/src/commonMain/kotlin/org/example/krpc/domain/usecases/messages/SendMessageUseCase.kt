package org.example.krpc.domain.usecases.messages

import org.example.krpc.domain.repo.MessagesRepository

interface SendMessageUseCase {
    suspend operator fun invoke(message: String)
}

class SendMessageUseCaseImpl(
    private val messagesRepository: MessagesRepository,
) : SendMessageUseCase {

    override suspend fun invoke(message: String) {
        return messagesRepository.sendMessage(message)
    }
}
