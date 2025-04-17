package ktor.backend.ktor.backend.services.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.example.krpc.models.requests.FileChunk
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

    /** сохраняем файл в папку проекта uploads */
    override suspend fun loadFile(file: ByteArray, name: String) {
        File("uploads").mkdir()
        File("uploads/${System.currentTimeMillis()}_$name").writeBytes(file)
    }

    override suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int> = flow {
        val fileChunks = mutableListOf<ByteArray>()

        chunks.collect { chunk ->
            fileChunks.add(chunk.data)

            val progress = (chunk.chunkIndex.toFloat() / chunk.totalChunks * 100).toInt()
            emit(progress)
        }

        val fileBytes = fileChunks.reduce { acc, bytes -> acc + bytes }

        File("uploads").mkdir()
        File("uploads/${System.currentTimeMillis()}_$name").writeBytes(fileBytes)

        emit(100)
    }
}