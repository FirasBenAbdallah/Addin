package addinn.dev.domain.usecase.event

import addinn.dev.domain.entity.event.Event
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.even.EventRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repo: EventRepo
) {
    suspend operator fun invoke(): Flow<Response<List<Event>>> =
        repo.getEvents()
}