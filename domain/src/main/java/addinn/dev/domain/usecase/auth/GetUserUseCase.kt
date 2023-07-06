package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.data.user.User
import addinn.dev.domain.repository.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(uid: String): Flow<Response<User>> =
        authRepository.getUser(uid)
}