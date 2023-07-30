package addinn.dev.data.repository.poll

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.poll.PollRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class PollRepoImpl(
    private val database: FirebaseFirestore
) : PollRepo {
    override suspend fun addPoll(pollRequest: PollRequest): Flow<Response<Boolean>> =
        callbackFlow {
            trySend(Response.Loading)

            val newDoc = database.collection("polls").document()

            newDoc.set(pollRequest)
                .addOnSuccessListener {
                    database.collection("polls").document(newDoc.id).update("id", newDoc.id)
                        .addOnSuccessListener {
                            trySend(Response.Success(true))
                        }
                }.addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }

    override suspend fun getPolls(): Flow<Response<List<Poll>>> = callbackFlow  {
        trySend(Response.Loading)

        val query = database.collection("polls")

        val listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                return@addSnapshotListener
            }

            if (value != null) {
                val polls = value.toObjects(Poll::class.java)
                trySend(Response.Success(polls))
            }
        }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }

    override suspend fun updatePoll(id:String,pollRequest: PollRequest): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("polls").document(id).update(
            "choiceVote1", pollRequest.choiceVote1,
            "choiceVote2", pollRequest.choiceVote2,
            "choiceVote3", pollRequest.choiceVote3,
            "totalVotes", pollRequest.totalVotes,
            "votesBy", pollRequest.votesBy
        ).addOnSuccessListener {
            trySend(Response.Success(true))
        }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }

        awaitClose {
            close()
        }
    }
}