package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(): Flow<Response<Boolean>> =
        authRepository.logout()
}