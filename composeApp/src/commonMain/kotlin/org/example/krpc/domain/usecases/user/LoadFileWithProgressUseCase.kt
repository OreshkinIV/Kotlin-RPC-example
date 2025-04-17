package org.example.krpc.domain.usecases.user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.rpc.krpc.streamScoped
import org.example.krpc.domain.repo.UserRepository
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.presentation.base.ext.asFlow
import org.example.krpc.presentation.base.ext.mapIndexed

interface LoadFileWithProgressUseCase {
    suspend operator fun invoke(
        file: ByteArray, name: String
    ): Flow<Int>
}

class LoadFileWithProgressUseCaseImpl(
    private val userRepository: UserRepository,
) : LoadFileWithProgressUseCase {

    override suspend fun invoke(
        file: ByteArray, name: String
    ): Flow<Int> = channelFlow {
        val chunkSize = 1024 * 8 // 8KB
        val totalChunks = (file.size + chunkSize - 1) / chunkSize

        val chunksFlow = file
            .asFlow(chunkSize)
            .mapIndexed { index, chunk ->
                FileChunk(
                    data = chunk,
                    chunkIndex = index,
                    totalChunks = totalChunks
                )
            }
        streamScoped {
            userRepository.loadFileWithProgress(chunksFlow, name).collect {
                send(it)
            }
        }
    }
}
