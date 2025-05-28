package org.example.krpc.domain.usecases.user

import org.example.krpc.domain.repo.UserRepository
import org.example.krpc.models.responses.JwtPayload

interface GetUserJwtPayloadUseCase {
    suspend operator fun invoke(): JwtPayload
}

class GetUserJwtPayloadUseCaseImpl(
    private val userRepository: UserRepository,
): GetUserJwtPayloadUseCase {

    override suspend fun invoke(): JwtPayload {
        return userRepository.getUserJwtPayload()
    }
}
