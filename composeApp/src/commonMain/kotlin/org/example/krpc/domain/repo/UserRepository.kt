package org.example.krpc.domain.repo

import kotlinx.coroutines.flow.Flow
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.models.responses.JwtPayload

interface UserRepository {

    suspend fun getUserJwtPayload(): JwtPayload

    suspend fun loadFile(file: ByteArray, name: String)

    suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int>
}