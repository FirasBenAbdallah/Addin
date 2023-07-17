package addinn.dev.data.repository.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.repository.chat.UsersRepo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UsersRepoImpl(private val database: FirebaseFirestore) : UsersRepo {
    override suspend fun getUsers(uid: String): Flow<Response<List<User>>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("users").whereNotEqualTo("id", uid).get().addOnSuccessListener {
            val users = it.toObjects(User::class.java)
            trySend(Response.Success(users))
        }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun getUsersByDepartment(
        uid: String,
        department: String
    ): Flow<Response<List<User>>> = callbackFlow {
        trySend(Response.Loading)

        database.collection("users")
            .whereNotEqualTo("id", uid)
            .whereEqualTo("department", department)
            .get()
            .addOnSuccessListener {
                val users = it.toObjects(User::class.java)
                trySend(Response.Success(users))
            }.addOnFailureListener {
                trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
            }

        awaitClose {
            close()
        }
    }

    override suspend fun searchUser(uid: String, search: String): Flow<Response<List<User>>> =
        callbackFlow {
            trySend(Response.Loading)

            val query = database.collection("users")
                .whereGreaterThanOrEqualTo("email", search)
                .whereLessThanOrEqualTo("email", search + "\uf8ff")

            val listenerRegistration = query.addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                    return@addSnapshotListener
                }

                if (value != null) {
                    val users = value.toObjects(User::class.java)
                    trySend(Response.Success(users))
                }
            }

            awaitClose {
                listenerRegistration.remove()
                close()
            }
        }

    override suspend fun getUserDataInRealTime(username: String): Flow<Response<User>> = callbackFlow {
        trySend(Response.Loading)

        val listenerRegistration = database.collection("users")
            .whereEqualTo("username", username)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(Response.Error(error.localizedMessage ?: "Error Occurred!"))
                    return@addSnapshotListener
                }

                if (value != null) {
                    val user = value.toObjects(User::class.java)
                    trySend(Response.Success(user[0]))
                }
            }

        awaitClose {
            listenerRegistration.remove()
            close()
        }
    }
}