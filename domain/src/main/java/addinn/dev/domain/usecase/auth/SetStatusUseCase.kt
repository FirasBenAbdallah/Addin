package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetStatusUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(
        isOnline: Boolean,
        lastSeen: Long,
        uid: String
    ): Flow<Response<Boolean>> =
        authRepository.setOnlineAndLastSeenStatus(
            isOnline = isOnline,
            lastSeen = lastSeen,
            uid = uid
        )
}