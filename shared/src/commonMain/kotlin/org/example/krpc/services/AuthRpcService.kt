package org.example.krpc.services

import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.UserResponse
import kotlinx.rpc.RemoteService
import kotlinx.rpc.annotations.Rpc
import org.example.krpc.models.responses.RpcResponse

@Rpc
interface AuthRpcService: RemoteService {

    suspend fun registerNewUser(body: AuthBody): RpcResponse<UserResponse?>

    suspend fun login(body: AuthBody): RpcResponse<UserResponse?>

    suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?>
}
