package org.example.krpc.domain.usecases.auth.rest

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.data.api.rest.RestApi
import org.example.krpc.data.preferences.saveTokens
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.models.responses.UserResponse

/** REST example */
interface RestRegisterUserUseCase {
    suspend operator fun invoke(body: AuthBody): UserResponse
}

class RestRegisterUserUseCaseImpl(
    private val api: RestApi,
    private val dataStore: DataStore<Preferences>
) : RestRegisterUserUseCase {

    override suspend fun invoke(body: AuthBody): UserResponse {
        return withContext(Dispatchers.IO) {
            api.registerUser(body).also {
                dataStore.saveTokens(it)
            }
        }
    }
}
