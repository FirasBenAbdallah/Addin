package addinn.dev.domain.usecase.chat

import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.chat.MessagesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repo: MessagesRepo
) {
    suspend operator fun invoke(chatRequest: ChatRequest,messageRequest: MessageRequest): Flow<Response<Boolean>> =
        repo.sendMessage(chatRequest,messageRequest)
}