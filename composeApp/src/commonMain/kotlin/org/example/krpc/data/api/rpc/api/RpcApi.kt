package org.example.krpc.data.api.rpc.api

import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import org.example.krpc.data.network.rpcService
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.requests.FileChunk
import org.example.krpc.models.responses.JwtPayload
import org.example.krpc.models.responses.UserResponse
import org.example.krpc.services.AuthRpcService
import org.example.krpc.services.MessagesRpcService
import org.example.krpc.services.UserRpcService

interface RpcApi {

    /** auth */
    suspend fun registerNewUser(body: AuthBody): UserResponse

    suspend fun login(body: AuthBody): UserResponse

    suspend fun refreshToken(refreshToken: String): UserResponse

    /** user */
    suspend fun getUserJwtPayload(): JwtPayload

    suspend fun sendMessage(message: String)

    suspend fun listenMessages(): Flow<String>

    suspend fun loadFile(file: ByteArray, name: String)

    suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int>
}

class RpcApiImpl(
    private val httpClient: HttpClient,
) : RpcApi {

    /** rpc services */
    private suspend fun authService(): AuthRpcService {
        return httpClient.rpcService<AuthRpcService>("auth")
    }

    private suspend fun userService(): UserRpcService {
        return httpClient.rpcService<UserRpcService>("user")
    }

    private suspend fun messagesService(): MessagesRpcService {
        return httpClient.rpcService<MessagesRpcService>("messages")
    }

    /**
     * http client можно закрыть для освобождения ресурсов (если используется не 1 экземпляр httpClient),
     * незавершенные вызовы продолжат выполняться
     * override suspend fun registerNewUser(body: AuthBody): RpcResponse<UserResponse?> {
     *         return withCloseClient { authService().registerNewUser(body) }
     *     }
     */
    private suspend fun <T : Any> withCloseClient(
        request: suspend () -> T
    ): T {
        return httpClient.use { /** = httpClient.close() */
            request.invoke()
        }
    }

    /** rpc auth */
    override suspend fun registerNewUser(body: AuthBody): UserResponse {
        return authService().registerNewUser(body)
    }

    override suspend fun login(body: AuthBody): UserResponse {
        return authService().login(body)
    }

    override suspend fun refreshToken(refreshToken: String): UserResponse {
        return authService().refreshToken(refreshToken)
    }

    /** rpc user */
    override suspend fun getUserJwtPayload(): JwtPayload {
        return userService().getUserJwtPayload()
    }

    override suspend fun sendMessage(message: String) {
        return messagesService().sendMessage(message)
    }

    override suspend fun listenMessages(): Flow<String> {
        return messagesService().listenMessages()
    }

    override suspend fun loadFile(file: ByteArray, name: String) {
        return userService().loadFile(file, name)
    }

    override suspend fun loadFileWithProgress(
        chunks: Flow<FileChunk>,
        name: String,
    ): Flow<Int> {
        return userService().loadFileWithProgress(chunks, name)
    }
}
