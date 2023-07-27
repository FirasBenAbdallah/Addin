package addinn.dev.domain.usecase.group

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.group.GroupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGrpMessageUseCase @Inject constructor(
    private val repo: GroupRepo
) {
    suspend operator fun invoke(groupId: String): Flow<Response<List<Message>>> =
        repo.getMessages(groupId)
}