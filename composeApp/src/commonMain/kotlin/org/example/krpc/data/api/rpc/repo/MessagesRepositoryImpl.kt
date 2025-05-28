package org.example.krpc.data.api.rpc.repo

import kotlinx.coroutines.flow.Flow
import org.example.krpc.data.api.rpc.api.RpcApi
import org.example.krpc.domain.repo.MessagesRepository

class MessagesRepositoryImpl(
    private val rpcApi: RpcApi,
) : MessagesRepository {

    override suspend fun sendMessage(message: String) {
        return rpcApi.sendMessage(message)
    }

    override suspend fun listenMessages(): Flow<String> {
        return rpcApi.listenMessages()
    }
}
