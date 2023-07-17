package addinn.dev.domain.usecase.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.repository.chat.UsersRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserRealtimeUseCase @Inject constructor(
    private val repo: UsersRepo
) {
    suspend operator fun invoke(username: String): Flow<Response<User>> =
        repo.getUserDataInRealTime(username)
}