package addinn.dev.domain.usecase.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.chat.MessagesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendGroupMessageUseCase @Inject constructor(
    private val repo: MessagesRepo
) {
    suspend operator fun invoke(groupName:String,messageRequest: MessageRequest): Flow<Response<Boolean>> =
        repo.sendGroupMessage(groupName = groupName,messageRequest = messageRequest)
}