package addinn.dev.domain.entity.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id: String? = null,
    var email: String?= null,
    var password: String?= null,
    var department: String?= null
) : Parcelable