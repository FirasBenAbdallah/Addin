package addinn.dev.domain.usecase.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.chat.MessagesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupMessageUseCase @Inject constructor(
    private val repo: MessagesRepo
) {
    suspend operator fun invoke(groupName:String): Flow<Response<List<Message>>> =
        repo.getGroupMessages(groupName = groupName)
}