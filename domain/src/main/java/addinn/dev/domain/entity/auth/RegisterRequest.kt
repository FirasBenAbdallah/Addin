package addinn.dev.domain.entity.auth

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("isOnline") val isOnline: Boolean = true,
    @SerializedName("lastSeen") val lastSeen: Long = System.currentTimeMillis(),
    @SerializedName("department") val department: String,
)