package org.example.krpc.data.api.rpc.repo

import kotlinx.coroutines.flow.Flow
import org.example.krpc.data.api.rpc.api.RpcApi
import org.example.krpc.domain.repo.UserRepository
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.models.responses.JwtPayload

class UserRepositoryImpl(
    private val rpcApi: RpcApi,
) : UserRepository {

    override suspend fun getUserJwtPayload(): JwtPayload {
        return rpcApi.getUserJwtPayload()
    }

    override suspend fun sendMessage(message: String) {
        return rpcApi.sendMessage(message)
    }

    override suspend fun listenMessages(): Flow<String> {
        return rpcApi.listenMessages()
    }

    override suspend fun loadFile(file: ByteArray, name: String) {
        return rpcApi.loadFile(file, name)
    }

    override suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int> {
        return rpcApi.loadFileWithProgress(chunks, name)
    }
}
