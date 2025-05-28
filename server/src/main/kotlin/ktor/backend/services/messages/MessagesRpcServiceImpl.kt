package ktor.backend.ktor.backend.services.messages

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.krpc.services.MessagesRpcService
import kotlin.coroutines.CoroutineContext

class MessagesRpcServiceImpl(
    override val coroutineContext: CoroutineContext,
    private val allMessages: MutableSet<String>,
    private val receivedMessages: MutableSet<String>
): MessagesRpcService {

    override suspend fun sendMessage(message: String) {
        allMessages.add(message)
    }

    override fun listenMessages(): Flow<String> {
        return flow {
            while (true) {
                allMessages.forEach { message ->
                    if (!receivedMessages.contains(message)) {
                        receivedMessages.add(message)
                        emit(message)
                    }
                }
            }
        }
    }
}