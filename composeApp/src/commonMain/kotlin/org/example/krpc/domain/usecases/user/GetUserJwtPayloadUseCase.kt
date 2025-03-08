package org.example.krpc.domain.usecases.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.domain.repo.UserRepository
import org.example.krpc.models.responses.JwtPayload

interface GetUserJwtPayloadUseCase {
    suspend operator fun invoke(): JwtPayload
}

class GetUserJwtPayloadUseCaseImpl(
    private val userRepository: UserRepository,
): GetUserJwtPayloadUseCase {

    override suspend fun invoke(): JwtPayload {
        return withContext(Dispatchers.IO) {
            userRepository.getUserJwtPayload()
        }
    }
}
