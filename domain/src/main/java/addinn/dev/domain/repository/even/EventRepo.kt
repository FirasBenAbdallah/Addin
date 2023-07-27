package addinn.dev.domain.repository.even

import addinn.dev.domain.entity.event.Event
import addinn.dev.domain.entity.event.EventRequest
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow

interface EventRepo {
    suspend fun getEvents(): Flow<Response<List<Event>>>
    suspend fun addEvent(eventRequest: EventRequest): Flow<Response<Boolean>>
}