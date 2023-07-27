package addinn.dev.data.repository.poll

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.poll.PollRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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
                choiceVote1 = pollRequest.choiceVote1,
                choiceVote2 = pollRequest.choiceVote2,
                choiceVote3 = pollRequest.choiceVote3,
                expirationTime = System.currentTimeMillis() // Store the creation timestamp
            )
            val newDoc = database.collection("polls").document()

            newDoc.set(poll)
                .addOnSuccessListener {
                    database.collection("polls").document(newDoc.id).update("id", newDoc.id)
                        .addOnSuccessListener {
                            trySend(Response.Success(poll))
                        }
                }.addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }



    /*override suspend fun addPoll(pollRequest: PollRequest): Flow<Response<Pair<String, PollResponse>>> =
        callbackFlow {
            trySend(Response.Loading)
            val poll = PollResponse(
                question = pollRequest.question,
                choice1 = pollRequest.choice1,
                choice2 = pollRequest.choice2,
                choice3 = pollRequest.choice3,
                choiceVote1 = pollRequest.choiceVote1,
                choiceVote2 = pollRequest.choiceVote2,
                choiceVote3 = pollRequest.choiceVote3,
            )
            val newDoc = database.collection("polls").document()

            newDoc.set(poll)
                .addOnSuccessListener {
                    database.collection("polls").document(newDoc.id).update("id", newDoc.id)
                        .addOnSuccessListener {
                            trySend(Response.Success(newDoc.id to poll))
                        }
                }.addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }*/


    override suspend fun getPolls(): Flow<Response<List<Poll>>> = callbackFlow {
        trySend(Response.Loading)
        database.collection("polls").get().addOnSuccessListener { snapshot ->
            val polls = snapshot.documents.mapNotNull { document ->
                document.toObject<Poll>()
            }
            if (polls.isNotEmpty()) {
                trySend(Response.Success(polls))
            } else {
                trySend(Response.Error("No polls found"))
            }
        }.addOnFailureListener { exception ->
            trySend(Response.Error(exception.localizedMessage ?: "Error occurred"))
        }
        awaitClose {
            close()
        }
    }
}