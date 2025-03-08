package org.example.krpc.data.api.rpc.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.example.krpc.data.api.rpc.api.RpcApi
import org.example.krpc.data.preferences.saveTokens
import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.RpcResponse
import org.example.krpc.models.responses.UserResponse
import org.example.krpc.models.responses.getOrError

class AuthRepositoryImpl(
    private val rpcApi: RpcApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    private suspend fun saveTokens(userResponse: UserResponse?) = dataStore.saveTokens(userResponse)

    override suspend fun registerNewUser(body: AuthBody): UserResponse? {
        return rpcApi.registerNewUser(body).getOrError().also {
            saveTokens(it)
        }
    }

    override suspend fun login(body: AuthBody): UserResponse? {
        return rpcApi.login(body).getOrError().also {
            saveTokens(it)
        }
    }

    override suspend fun refreshToken(refreshToken: String): RpcResponse<UserResponse?> {
        return rpcApi.refreshToken(refreshToken).also {
            saveTokens(it.data)
        }
    }
}