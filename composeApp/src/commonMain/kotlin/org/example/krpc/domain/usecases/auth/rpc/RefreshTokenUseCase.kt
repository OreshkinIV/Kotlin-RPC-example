package org.example.krpc.domain.usecases.auth.rpc

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.models.responses.UserResponse

interface RefreshTokenUseCase {
    suspend operator fun invoke(refreshToken: String): UserResponse
}

class RefreshTokenUseCaseImpl(
    private val authRepository: AuthRepository,
): RefreshTokenUseCase {

    override suspend fun invoke(refreshToken: String): UserResponse {
        return withContext(Dispatchers.IO) {
            authRepository.refreshToken(refreshToken)
        }
    }
}
