package addinn.dev.domain.repository.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow

interface MessagesRepo {
    suspend fun getMessages(chatId:String): Flow<Response<List<Message>>>
    suspend fun sendMessage(chatRequest: ChatRequest,messageRequest: MessageRequest): Flow<Response<Boolean>>
    suspend fun getLatestConversation(from:String): Flow<Response<List<Chat>>>
    suspend fun sendIsTyping(chatId:String,isTyping:String): Flow<Response<Boolean>>
    suspend fun removeIsTyping(chatId:String,isTyping:String): Flow<Response<Boolean>>
    suspend fun listenIsTyping(chatId:String): Flow<Response<List<String>>>
    suspend fun getGroupMessages(groupName: String): Flow<Response<List<Message>>>
    suspend fun sendGroupMessage(groupName:String,messageRequest: MessageRequest): Flow<Response<Boolean>>
    suspend fun setMessageAsSeen(chatId: String): Flow<Response<Boolean>>
}