
package addinn.dev.domain.repository.poll

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow

interface PollRepo {
    suspend fun addPoll(pollRequest: PollRequest): Flow<Response<Boolean>>
    suspend fun getPolls(): Flow<Response<List<Poll>>>
    suspend fun updatePoll(id:String,pollRequest: PollRequest): Flow<Response<Boolean>>
}
