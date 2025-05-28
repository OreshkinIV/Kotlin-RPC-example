package org.example.krpc.domain.usecases.user

import org.example.krpc.domain.repo.UserRepository

interface LoadFileUseCase {
    suspend operator fun invoke(file: ByteArray, name: String)
}

class LoadFileUseCaseImpl(
    private val userRepository: UserRepository,
) : LoadFileUseCase {

    override suspend fun invoke(file: ByteArray, name: String) {
        return userRepository.loadFile(file, name)
    }
}
