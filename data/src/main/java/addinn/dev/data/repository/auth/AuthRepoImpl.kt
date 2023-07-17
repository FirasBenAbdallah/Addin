package addinn.dev.data.repository.auth

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.repository.auth.AuthRepo
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepoImpl(
    private val database: FirebaseFirestore,
    private val cloudStorage: FirebaseStorage,
    private val auth: FirebaseAuth
) : AuthRepo {

    override suspend fun register(
        imageUri: Uri,
        registerRequest: RegisterRequest
    ): Flow<Response<RegisterResponse>> =
        callbackFlow {
            trySend(Response.Loading)
            auth.createUserWithEmailAndPassword(registerRequest.email, registerRequest.password)
                .addOnSuccessListener {
                    cloudStorage.reference.child("avatars").child("${registerRequest.username}.png")
                        .putFile(imageUri)
                        .addOnSuccessListener { }
                        .addOnFailureListener { here ->
                            trySend(Response.Error(here.localizedMessage ?: "Error Occurred!"))
                        }

                    val user = RegisterResponse(
                        id = it.user?.uid,
                        email = it.user?.email,
                        password = registerRequest.password,
                        isOnline = registerRequest.isOnline,
                        lastSeen = registerRequest.lastSeen,
                        department = registerRequest.department,
                        username = registerRequest.username,
                        avatarUrl = "https://firebasestorage.googleapis.com/v0/b/team-addinn.appspot.com/o/avatars%2F${registerRequest.username}.png?alt=media"
                    )
                    database.collection("users").document(user.id!!).set(user)
                        .addOnSuccessListener {
                            trySend(Response.Success(user))
                        }.addOnFailureListener { newError ->
                            trySend(Response.Error(newError.localizedMessage ?: "Error Occurred!"))
                        }
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }

        }

    override suspend fun login(loginRequest: LoginRequest): Flow<Response<User>> =
        callbackFlow {
            trySend(Response.Loading)
            auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
                .addOnSuccessListener {
                    database.collection("users").document(it.user?.uid!!).get()
                        .addOnSuccessListener { userDB ->
                            val user = userDB.toObject(User::class.java)
                            if (user == null) {
                                trySend(Response.Error("User Not Found!"))
                            } else {
                                trySend(Response.Success(user))
                            }
                        }.addOnFailureListener { newError ->
                            trySend(Response.Error(newError.localizedMessage ?: "Error Occurred!"))
                        }
                }
                .addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }
        }

    override suspend fun logout(): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)
        auth.signOut()
        trySend(Response.Success(true))
        awaitClose {
            close()
        }
    }

    override suspend fun getUser(uid: String): Flow<Response<User>> = callbackFlow {
        trySend(Response.Loading)
        database.collection("users").document(uid).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)
            if (user == null) {
                trySend(Response.Error("User Not Found!"))
            } else {
                trySend(Response.Success(user))
            }
        }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun subscribeToTopic(
        userId: String,
        department: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        FirebaseMessaging.getInstance().subscribeToTopic(userId)
            .addOnSuccessListener {
                FirebaseMessaging.getInstance().subscribeToTopic(department).addOnSuccessListener {
                    trySend(Response.Success(true))
                }.addOnFailureListener {
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

    override suspend fun checkUsername(username: String): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        val query = database.collection("users")
            .whereEqualTo("username", username)

        val listener = query.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                return@addSnapshotListener
            }

            if (value != null) {
                if (value.isEmpty) {
                    trySend(Response.Success(false))
                } else {
                    trySend(Response.Success(true))
                }
            }
        }

        awaitClose {
            listener.remove()
            close()
        }
    }

    override suspend fun setOnlineAndLastSeenStatus(
        isOnline: Boolean,
        lastSeen: Long,
        uid: String
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("users").document(uid).update("isOnline", isOnline, "lastSeen", lastSeen)
            .addOnSuccessListener {
                trySend(Response.Success(true))
            }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }

        awaitClose {
            close()
        }
    }

}
