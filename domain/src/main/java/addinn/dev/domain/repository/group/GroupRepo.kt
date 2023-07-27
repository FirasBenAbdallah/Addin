package addinn.dev.domain.repository.group

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.group.Group
import addinn.dev.domain.entity.group.GroupRequest
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow

interface GroupRepo {
    suspend fun getMessages(groupId:String): Flow<Response<List<Message>>>
    suspend fun sendMessage(groupId: String, messageRequest: MessageRequest): Flow<Response<Boolean>>
    suspend fun getGroups(id:String): Flow<Response<List<Group>>>
    suspend fun createGroup(groupRequest: GroupRequest, messageRequest: MessageRequest): Flow<Response<String>>
}