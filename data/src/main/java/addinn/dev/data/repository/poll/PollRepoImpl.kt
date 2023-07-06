package addinn.dev.data.repository.poll

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.poll.PollRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class PollRepoImpl(
    private val database: FirebaseFirestore
) : PollRepo {
    override suspend fun addPoll(pollRequest: PollRequest): Flow<Response<PollResponse>> =
        callbackFlow {
            trySend(Response.Loading)
            val poll = PollResponse(
                question = pollRequest.question,
                choice1 = pollRequest.choice1,
                choice2 = pollRequest.choice2,
                choice3 = pollRequest.choice3,
            )
            database.collection("polls").document().set(poll)
                .addOnSuccessListener {
                    trySend(Response.Success(poll))
                }.addOnFailureListener { newError ->
                    trySend(Response.Error(newError.localizedMessage ?: "Error Occurred!"))
                }.addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }

        }
}