package addinn.dev.domain.usecase.group

import addinn.dev.domain.entity.group.Group
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.group.GroupRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupsUseCase @Inject constructor(
    private val repo: GroupRepo
) {
    suspend operator fun invoke(username: String): Flow<Response<List<Group>>> =
        repo.getGroups(username)
}