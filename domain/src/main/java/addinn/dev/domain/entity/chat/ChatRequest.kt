package addinn.dev.domain.entity.chat

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("participant1") val participant1: String,
    @SerializedName("participant2") val participant2: String,
    @SerializedName("lastMessageSenderId") val lastMessageSenderId: String,
    @SerializedName("lastMessageDate") val lastMessageDate: Long = System.currentTimeMillis(),
    @SerializedName("lastMessageStatus") val lastMessageStatus: String = "SENT",
    @SerializedName("lastMessage") val lastMessage: String,
    @SerializedName("id") val id: String,
)