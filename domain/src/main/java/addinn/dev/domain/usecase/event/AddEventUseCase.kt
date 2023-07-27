package addinn.dev.domain.usecase.event

import addinn.dev.domain.entity.event.EventRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.even.EventRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val repo: EventRepo
) {
    suspend operator fun invoke(
        eventRequest: EventRequest
    ): Flow<Response<Boolean>> =
        repo.addEvent(eventRequest)
}