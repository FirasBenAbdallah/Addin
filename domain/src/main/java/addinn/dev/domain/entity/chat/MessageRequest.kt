package addinn.dev.domain.entity.chat

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    @SerializedName("sender") val sender: String,
    @SerializedName("message") val message: String,
    @SerializedName("isFile") val isFile: Boolean = false,
    @SerializedName("date") val date: Long = System.currentTimeMillis()
)