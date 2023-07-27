package addinn.dev.data.repository.group

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.group.Group
import addinn.dev.domain.entity.group.GroupRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.group.GroupRepo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GroupMsgRepoImpl(private val database: FirebaseFirestore) : GroupRepo {

    override suspend fun getMessages(groupId: String): Flow<Response<List<Message>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("groups")
                .document(groupId)
                .collection("messages")
                .orderBy("date", Query.Direction.DESCENDING)

            val listenerRegistration = query.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                    return@addSnapshotListener
                }

                if (value != null) {
                    val messages = value.toObjects(Message::class.java)
                    trySend(Response.Success(messages))
                }
            }

            awaitClose {
                listenerRegistration.remove()
                close()
            }
        }

    override suspend fun sendMessage(
        groupId: String,
        messageRequest: MessageRequest
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("groups")
            .document(groupId)
            .collection("messages")
            .add(messageRequest)
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

    override suspend fun getGroups(id: String): Flow<Response<List<Group>>> = callbackFlow {
        trySend(Response.Loading)

        val query = database.collection("groups")
            .whereArrayContains("members", id)

        val listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                return@addSnapshotListener
            }

            if (value != null) {
                val groups = value.toObjects(Group::class.java)
                trySend(Response.Success(groups))
            }
        }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }

    override suspend fun createGroup(
        groupRequest: GroupRequest,
        messageRequest: MessageRequest
    ): Flow<Response<String>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("groups").document(groupRequest.id).set(groupRequest).addOnSuccessListener {
            database.collection("groups")
                .document(groupRequest.id)
                .collection("messages")
                .add(messageRequest)
                .addOnSuccessListener {
                    trySend(Response.Success(groupRequest.id))
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }
        }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }

        awaitClose{
            close()
        }
    }

}