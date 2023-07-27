package addinn.dev.domain.usecase.poll;

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.poll.PollRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPollUseCase @Inject constructor(
    private val pollRepository: PollRepo
) {
    suspend operator fun invoke(): Flow<Response<List<Poll>>> =
        pollRepository.getPolls()
}
