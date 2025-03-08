package org.example.krpc.domain.usecases.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.krpc.domain.repo.UserRepository

interface LoadFileUseCase {
    suspend operator fun invoke(file: ByteArray, name: String)
}

class LoadFileUseCaseImpl(
    private val userRepository: UserRepository,
) : LoadFileUseCase {

    override suspend fun invoke(file: ByteArray, name: String) {
        return withContext(Dispatchers.IO) {
            userRepository.loadFile(file, name)
        }
    }
}
