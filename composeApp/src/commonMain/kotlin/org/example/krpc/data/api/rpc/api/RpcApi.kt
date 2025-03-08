package org.example.krpc.data.api.rpc.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.core.use
import kotlinx.coroutines.flow.Flow
import org.example.krpc.data.network.rpcFlow
import org.example.krpc.data.network.rpcService
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.JwtPayload
import org.example.krpc.models.responses.RpcResponse
import org.example.krpc.models.responses.UserResponse
import org.example.krpc.services.AuthRpcService
import org.example.krpc.services.UserRpcService

interface RpcApi {

    /** auth */
    suspend fun registerNewUser(body: AuthBody): RpcResponse<UserResponse?>

    suspend fun login(body: AuthBody): RpcResponse<UserResponse?>

    suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?>

    /** user */
    suspend fun getUserJwtPayload(): JwtPayload

    suspend fun sendMessage(message: String)

    fun listenMessages(): Flow<String>

    suspend fun loadFile(file: ByteArray, name: String)
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

    /**
     * http client можно закрыть для освобождения ресурсов, незавершенные вызовы продолжат выполняться
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
    override suspend fun registerNewUser(body: AuthBody): RpcResponse<UserResponse?> {
        return authService().registerNewUser(body)
    }

    override suspend fun login(body: AuthBody): RpcResponse<UserResponse?> {
        return authService().login(body)
    }

    override suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?> {
        return authService().refreshToken(refreshToken)
    }

    /** rpc user */
    override suspend fun getUserJwtPayload(): JwtPayload {
        return userService().getUserJwtPayload()
    }

    override suspend fun sendMessage(message: String) {
        return userService().sendMessage(message)
    }

    override fun listenMessages(): Flow<String> {
        return rpcFlow {
            userService().listenMessages()
        }
    }

    override suspend fun loadFile(file: ByteArray, name: String) {
        return userService().loadFile(file, name)
    }
}
