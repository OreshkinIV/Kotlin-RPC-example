package org.example.krpc.domain.usecases.auth.rpc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.models.requests.AuthBody
import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.models.responses.UserResponse

interface RegisterUserUseCase {
    suspend operator fun invoke(body: AuthBody): UserResponse
}

class RegisterUserUseCaseImpl(
    private val authRepository: AuthRepository,
): RegisterUserUseCase {

    override suspend fun invoke(body: AuthBody): UserResponse {
        return withContext(Dispatchers.IO) {
            authRepository.registerNewUser(body)
        }
    }
}
