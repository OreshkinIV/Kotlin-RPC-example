package org.example.krpc.services

import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.models.responses.JwtPayload

@Rpc
interface UserRpcService : RemoteService {

    suspend fun getUserJwtPayload(): JwtPayload

    suspend fun sendMessage(message: String)

    fun listenMessages(): Flow<String>

    suspend fun loadFile(file: ByteArray, name: String)

    suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int>
}