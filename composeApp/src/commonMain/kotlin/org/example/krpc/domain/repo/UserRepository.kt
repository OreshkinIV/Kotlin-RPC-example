package org.example.krpc.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.krpc.models.responses.JwtPayload

interface UserRepository {

    suspend fun getUserJwtPayload(): JwtPayload

    suspend fun sendMessage(message: String)

    fun listenMessages(): Flow<String>

    suspend fun loadFile(file: ByteArray, name: String)
}