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
) : UserRpcService {

    override suspend fun getUserJwtPayload(): JwtPayload {
        return jwtPayload
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