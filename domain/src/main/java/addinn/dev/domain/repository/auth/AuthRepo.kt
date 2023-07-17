package addinn.dev.domain.repository.auth

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.entity.response.Response
import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun register(
        imageUri: Uri,
        registerRequest: RegisterRequest
    ): Flow<Response<RegisterResponse>>

    suspend fun login(loginRequest: LoginRequest): Flow<Response<User>>
    suspend fun logout(): Flow<Response<Boolean>>
    suspend fun getUser(uid: String): Flow<Response<User>>
    suspend fun subscribeToTopic(userId: String, department: String): Flow<Response<Boolean>>
    suspend fun checkUsername(username: String): Flow<Response<Boolean>>
    suspend fun setOnlineAndLastSeenStatus(
        isOnline: Boolean,
        lastSeen: Long,
        uid: String
    ): Flow<Response<Boolean>>
}