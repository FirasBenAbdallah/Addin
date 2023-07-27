package addinn.dev.domain.entity.event

import com.google.gson.annotations.SerializedName

data class EventRequest(
    @SerializedName("id") val id: String = System.currentTimeMillis().toString(),
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String,
    @SerializedName("date") val date: Long,
    @SerializedName("time") val time: Long,
)