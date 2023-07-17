package addinn.dev.domain.entity.chat

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chat(
    var id: String? = null,
    var participant1: String? = null,
    var participant2: String? = null,
    var lastMessageSenderId: String? = null,
    var lastMessageDate: Long? = null,
    var lastMessageStatus: String? = null,
    var lastMessage: String? = null,
    var isTyping: List<String>? = null
) : Parcelable