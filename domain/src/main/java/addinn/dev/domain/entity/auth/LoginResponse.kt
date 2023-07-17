package addinn.dev.domain.entity.auth

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    var id: String?,
    var email: String?,
    var password: String?,
    var department: String?,
) : Parcelable