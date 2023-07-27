package addinn.dev.data.repository.event

import addinn.dev.domain.entity.event.Event
import addinn.dev.domain.entity.event.EventRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.even.EventRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class EventRepoImpl(private val database: FirebaseFirestore) : EventRepo {
    override suspend fun getEvents(): Flow<Response<List<Event>>> = callbackFlow {
        trySend(Response.Loading)

        val query = database.collection("events")

        val listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                return@addSnapshotListener
            }

            if (value != null) {
                val events = value.toObjects(Event::class.java)
                Timber.e("events: $events")
                trySend(Response.Success(events))
            }
        }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }

    override suspend fun addEvent(eventRequest: EventRequest): Flow<Response<Boolean>> =
        callbackFlow {
            trySend(Response.Loading)

            database.collection("events")
                .document(eventRequest.id)
                .set(eventRequest)
                .addOnSuccessListener {
                    trySend(Response.Success(true))
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }
}