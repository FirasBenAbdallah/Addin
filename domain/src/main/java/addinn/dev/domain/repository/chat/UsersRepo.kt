package addinn.dev.domain.repository.chat

import addinn.dev.domain.entity.user.User
import addinn.dev.domain.entity.response.Response
import kotlinx.coroutines.flow.Flow

interface UsersRepo {
    suspend fun getUsers(uid: String): Flow<Response<List<User>>>
    suspend fun getUsersByDepartment(uid: String, department: String): Flow<Response<List<User>>>
    suspend fun searchUser(uid: String, search: String): Flow<Response<List<User>>>
    suspend fun getUserDataInRealTime(username: String): Flow<Response<User>>
}