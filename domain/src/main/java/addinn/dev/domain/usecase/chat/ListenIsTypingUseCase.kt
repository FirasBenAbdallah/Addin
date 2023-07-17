package addinn.dev.domain.usecase.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.chat.MessagesRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenIsTypingUseCase @Inject constructor(
    private val repo: MessagesRepo
) {
    suspend operator fun invoke(chatId: String): Flow<Response<List<String>>> =
        repo.listenIsTyping(chatId)
}