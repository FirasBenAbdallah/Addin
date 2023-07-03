package addinn.dev.data.repository.auth

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.LoginResponse
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.repository.auth.AuthRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepoImpl(
    private val database: FirebaseFirestore,
    private val auth: FirebaseAuth
) : AuthRepo {
    override suspend fun register(registerRequest: RegisterRequest): Flow<Response<RegisterResponse>> =
        callbackFlow {
            trySend(Response.Loading)
            auth.createUserWithEmailAndPassword(registerRequest.email, registerRequest.password)
                .addOnSuccessListener {
                    val user = RegisterResponse(
                        id = it.user?.uid,
                        email = it.user?.email,
                        password = registerRequest.password,
                        department = registerRequest.department
                    )
                    database.collection("users").document(user.id!!).set(user)
                        .addOnSuccessListener {
                            trySend(Response.Success(user))
                        }.addOnFailureListener { newError ->
                        trySend(Response.Error(newError.localizedMessage ?: "Error Occurred!"))
                    }
                }.addOnFailureListener {
                    trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
                }

            awaitClose {
                close()
            }

        }

    override suspend fun login(loginRequest: LoginRequest): Flow<Response<LoginResponse>> = callbackFlow {
        trySend(Response.Loading)
        auth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password).addOnSuccessListener { loginResult ->
            /*val user = LoginResponse(
                id = it.user?.uid,
                email = it.user?.email,
                password = loginRequest.password,
                department = loginRequest.department
            )*/
           /* database.collection("users").document(it.user.id!!).get().addOnSuccessListener { user ->
                val user= user.toObject(User::class.java)
                trySend(Response.Success(user)
            }.addOnFailureListener { newError ->
                trySend(Response.Error(newError.localizedMessage ?: "Error Occurred!"))
            }*/
            val loggedInUser = LoginResponse(
                id = loginResult.user?.uid,
                email = loginResult.user?.email,
                password = loginRequest.password,
                department = ""
            )
            println("Welcome, ${loggedInUser.email}! with id ${loggedInUser.id} and password ${loggedInUser.password}")
            trySend(Response.Success(loggedInUser))
        }.addOnFailureListener {
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
            trySend(Response.Success(user!!))
        }.addOnFailureListener {
            trySend(Response.Error(it.localizedMessage ?: "Error Occurred!"))
        }
        awaitClose {
            close()
        }
    }

}
