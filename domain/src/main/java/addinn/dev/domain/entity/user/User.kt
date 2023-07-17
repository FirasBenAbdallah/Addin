package addinn.dev.domain.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var email: String? = null,
    var password: String? = null,
    var isOnline: Boolean? = null,
    var lastSeen: Long? = null,
    var department: String? = null,
    var username: String? = null,
    var avatarUrl: String? = null,
) : Parcelable {
    // Getter for isOnline
    fun getIsOnline(): Boolean? {
        return isOnline
    }

    // Setter for isOnline
    fun setIsOnline(online: Boolean?) {
        isOnline = online
    }
}