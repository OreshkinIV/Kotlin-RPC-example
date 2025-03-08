package org.example.krpc.domain.usecases.auth.rpc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.models.responses.UserResponse

interface LoginUserUseCase {
    suspend operator fun invoke(body: AuthBody): UserResponse?
}

class LoginUserUseCaseImpl(
    private val authRepository: AuthRepository,
) : LoginUserUseCase {

    override suspend fun invoke(body: AuthBody): UserResponse? {
        return withContext(Dispatchers.IO) {
            authRepository.login(body)
        }
    }
}
