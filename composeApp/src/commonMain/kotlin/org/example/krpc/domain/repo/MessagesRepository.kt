package org.example.krpc.domain.repo

import kotlinx.coroutines.flow.Flow

interface MessagesRepository {

    suspend fun sendMessage(message: String)

    suspend fun listenMessages(): Flow<String>
}