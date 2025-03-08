package org.example.krpc.domain.repo

import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.RpcResponse
import org.example.krpc.models.responses.UserResponse

interface AuthRepository {

    suspend fun registerNewUser(body: AuthBody): UserResponse?

    suspend fun login(body: AuthBody): UserResponse?

    suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?>
}