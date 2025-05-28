package org.example.krpc.services

import kotlinx.coroutines.flow.Flow
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.models.responses.JwtPayload

@Rpc
interface UserRpcService : RemoteService {

    /** получить jwtPayload */
    suspend fun getUserJwtPayload(): JwtPayload

    /** загрузка файла
     * @param file - файл в виде массива байтов
     * @param name - имя файла
     *  */
    suspend fun loadFile(file: ByteArray, name: String)

    /** загрузка файла с прогрессом
     * @param chunks - потом частей файла
     * @param name - имя файла
     * @return возвращает прогресс загрузки  */
    suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int>
}