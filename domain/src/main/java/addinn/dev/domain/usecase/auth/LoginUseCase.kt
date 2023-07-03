package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.LoginResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(loginRequest: LoginRequest) : Flow<Response<LoginResponse>> =
        authRepository.login(loginRequest)
}