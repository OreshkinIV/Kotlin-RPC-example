package org.example.krpc.services

import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc

@Rpc
interface MessagesRpcService: RemoteService {
    /**
     * Отправка сообщения
     * @param text - Текст сообщения
     */
    suspend fun sendMessage(text: String)

    /**
     * Подписка на сообщения
     * @return Возвращает поток новых сообщений
     */
    fun listenMessages(): Flow<String>
}