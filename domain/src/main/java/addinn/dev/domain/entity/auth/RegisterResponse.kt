package addinn.dev.domain.entity.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterResponse(
    var id: String?,
    var email: String?,
    var password: String?,
    var isOnline: Boolean?,
    var lastSeen: Long?,
    var department: String?,
    var username: String?,
    var avatarUrl: String?
) : Parcelable