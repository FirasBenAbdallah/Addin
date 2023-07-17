package addinn.dev.domain.entity.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    var id: String? = null,
    var sender: String? = null,
    var message: String? = null,
    var isFile: Boolean? = null,
    var date: Long? = null,
) : Parcelable