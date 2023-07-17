package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.auth.AuthRepo
import android.net.Uri
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(
        imageUri: Uri,
        registerRequest: RegisterRequest
    ): Flow<Response<RegisterResponse>> =
        authRepository.register(imageUri = imageUri, registerRequest = registerRequest)
}