package addinn.dev.domain.repository.auth

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.LoginResponse
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.data.user.User
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepo {
    suspend fun register(registerRequest: RegisterRequest): Flow<Response<RegisterResponse>>

    suspend fun logout(): Flow<Response<Boolean>>
    suspend fun getUser(uid:String): Flow<Response<User>>
    suspend fun login(loginRequest: LoginRequest): Flow<Response<LoginResponse>>

}