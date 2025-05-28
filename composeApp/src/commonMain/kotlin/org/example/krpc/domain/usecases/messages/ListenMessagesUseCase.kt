package org.example.krpc.domain.usecases.messages

import kotlinx.coroutines.flow.Flow
import org.example.krpc.domain.repo.MessagesRepository

interface ListenMessagesUseCase {
    suspend operator fun invoke(): Flow<String>
}

class ListenMessagesUseCaseImpl(
    private val messagesRepository: MessagesRepository,
) : ListenMessagesUseCase {

    override suspend fun invoke(): Flow<String> {
        return messagesRepository.listenMessages()
    }
}
