package addinn.dev.domain.usecase.poll

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.poll.PollRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddPollUseCase @Inject constructor(
    private val pollRepository: PollRepo
) {
    suspend operator fun invoke(pollRequest: PollRequest): Flow<Response<Boolean>> =
        pollRepository.addPoll(pollRequest)
}