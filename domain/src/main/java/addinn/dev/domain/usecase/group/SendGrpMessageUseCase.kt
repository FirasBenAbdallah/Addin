package addinn.dev.domain.usecase.group

import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.group.GroupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendGrpMessageUseCase @Inject constructor(
    private val repo: GroupRepo
) {
    suspend operator fun invoke(
        groupId: String,
        messageRequest: MessageRequest
    ): Flow<Response<Boolean>> =
        repo.sendMessage(groupId, messageRequest)
}