package addinn.dev.domain.usecase.group

import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.group.GroupRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.group.GroupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateGroupUseCase @Inject constructor(
    private val repo: GroupRepo
) {
    suspend operator fun invoke(groupRequest: GroupRequest, messageRequest: MessageRequest): Flow<Response<String>> =
        repo.createGroup(groupRequest,messageRequest)
}