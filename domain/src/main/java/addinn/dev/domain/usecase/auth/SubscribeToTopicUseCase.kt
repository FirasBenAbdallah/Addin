package addinn.dev.domain.usecase.auth

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscribeToTopicUseCase @Inject constructor(
    private val authRepository: AuthRepo
) {
    suspend operator fun invoke(uid: String,department:String): Flow<Response<Boolean>> =
        authRepository.subscribeToTopic(uid,department)
}