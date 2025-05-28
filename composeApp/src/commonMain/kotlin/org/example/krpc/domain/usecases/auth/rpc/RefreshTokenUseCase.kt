package org.example.krpc.domain.usecases.auth.rpc

import org.example.krpc.domain.repo.AuthRepository
import org.example.krpc.models.responses.UserResponse

interface RefreshTokenUseCase {
    suspend operator fun invoke(refreshToken: String): UserResponse
}

class RefreshTokenUseCaseImpl(
    private val authRepository: AuthRepository,
): RefreshTokenUseCase {

    override suspend fun invoke(refreshToken: String): UserResponse {
        return authRepository.refreshToken(refreshToken)
    }
}
