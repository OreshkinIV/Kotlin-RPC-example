package ktor.backend.ktor.backend.services.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.krpc.models.responses.JwtPayload
import org.example.krpc.services.UserRpcService
import java.io.File
import kotlin.coroutines.CoroutineContext

class UserRpcServiceImpl(
    override val coroutineContext: CoroutineContext,
    private val jwtPayload: JwtPayload,
    private val allMessages: MutableSet<String>,
    private val receivedMessages: MutableSet<String>
) : UserRpcService {

    override suspend fun getUserJwtPayload(): JwtPayload {
        return jwtPayload
    }

    override suspend fun sendMessage(message: String) {
        allMessages.add(message)
    }

    override suspend fun listenMessages(): Flow<String> {
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

    /** сохраняем файл в папку проекта uploads */
    override suspend fun loadFile(file: ByteArray, name: String) {
        File("uploads").mkdir()
        File("uploads/${System.currentTimeMillis()}_$name").writeBytes(file)
    }
}