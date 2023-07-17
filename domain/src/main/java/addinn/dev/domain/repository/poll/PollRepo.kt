
package addinn.dev.domain.repository.poll

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow


interface PollRepo {
    suspend fun addPoll(pollRequest: PollRequest): Flow<Response<PollResponse>>
}
