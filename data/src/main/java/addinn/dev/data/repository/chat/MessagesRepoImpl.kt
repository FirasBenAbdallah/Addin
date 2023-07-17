package addinn.dev.data.repository.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.repository.chat.MessagesRepo
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MessagesRepoImpl(private val database: FirebaseFirestore) : MessagesRepo {
    override suspend fun getMessages(chatId: String): Flow<Response<List<Message>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("chats")
                .document(chatId)
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
        chatRequest: ChatRequest,
        messageRequest: MessageRequest): Flow<Response<Boolean>> = callbackFlow {
            trySend(Response.Loading)

            database.collection("chats").document(chatRequest.id).set(chatRequest)
                .addOnSuccessListener {
                    database.collection("chats").document(chatRequest.id).collection("messages")
                        .add(messageRequest)
                        .addOnSuccessListener {
                            trySend(Response.Success(true))
                        }
                        .addOnFailureListener {
                            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                        }
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }

    override suspend fun getLatestConversation(from: String): Flow<Response<List<Chat>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("chats")
                .where(
                    Filter.or(
                        Filter.equalTo("participant1", from),
                        Filter.equalTo("participant2", from)
                    )
                )

            val listenerRegistration = query.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                    return@addSnapshotListener
                }

                if (value != null) {
                    val messages = value.toObjects(Chat::class.java)
                    trySend(Response.Success(messages))
                }
            }

            awaitClose {
                listenerRegistration.remove()
                close()
            }
        }

    override suspend fun sendIsTyping(
        chatId: String,
        isTyping: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("chats")
            .document(chatId)
            .get()
            .addOnSuccessListener { doc ->
                val chat = doc.toObject(Chat::class.java)
                val isTypingList = doc.get("isTyping") as? List<*>

                if (chat == null) {
                    trySend(Response.Error("Error Occurred!"))
                    return@addOnSuccessListener
                } else {

                    if (isTypingList == null) {
                        doc.reference.update("isTyping", listOf(isTyping))
                            .addOnSuccessListener { trySend(Response.Success(true)) }
                            .addOnFailureListener {
                                trySend(
                                    Response.Error(
                                        it.localizedMessage ?: "Error Occurred!"
                                    )
                                )
                            }
                        return@addOnSuccessListener
                    } else {
                        val newList = isTypingList.toMutableList()
                        if (newList.contains(isTyping)) {
                            return@addOnSuccessListener
                        }
                        newList.add(isTyping)
                        doc.reference.update("isTyping", newList)
                            .addOnSuccessListener { trySend(Response.Success(true)) }
                            .addOnFailureListener {
                                trySend(
                                    Response.Error(
                                        it.localizedMessage ?: "Error Occurred!"
                                    )
                                )
                            }
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
                trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
            }

        awaitClose {
            close()
        }

    }

    override suspend fun removeIsTyping(
        chatId: String,
        isTyping: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("chats")
            .document(chatId)
            .get()
            .addOnSuccessListener { doc ->
                val chat = doc.toObject(Chat::class.java)
                val isTypingList = doc.get("isTyping") as? List<*>

                if (chat == null) {
                    trySend(Response.Error("Error Occurred!"))
                    return@addOnSuccessListener
                } else {
                    if (isTypingList == null) {
                        return@addOnSuccessListener
                    } else {
                        val newList = isTypingList.toMutableList()
                        if (newList.contains(isTyping)) {
                            newList.remove(isTyping)
                            doc.reference.update("isTyping", newList)
                                .addOnSuccessListener { trySend(Response.Success(true)) }
                                .addOnFailureListener {
                                    trySend(
                                        Response.Error(
                                            it.localizedMessage ?: "Error Occurred!"
                                        )
                                    )
                                }
                            return@addOnSuccessListener
                        }
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener {
                trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
            }

        awaitClose {
            close()
        }

    }

    override suspend fun listenIsTyping(chatId: String): Flow<Response<List<String>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("chats")
                .document(chatId)

            val listenerRegistration = query.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                    return@addSnapshotListener
                }

                if (value != null) {
                    val isTypingList = value.get("isTyping")
                    if (isTypingList != null) {
                        trySend(Response.Success(isTypingList as List<String>))
                    }
                }
            }

            awaitClose {
                listenerRegistration.remove()
                close()
            }


        }

    override suspend fun getGroupMessages(groupName: String): Flow<Response<List<Message>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("channel")
                .document(groupName)
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

    override suspend fun sendGroupMessage(
        groupName: String,
        messageRequest: MessageRequest
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("channel")
            .document(groupName)
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

    override suspend fun setMessageAsSeen(chatId: String): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("chats")
            .document(chatId)
            .update("lastMessageStatus", "SEEN")
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
