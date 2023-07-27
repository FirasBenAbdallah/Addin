package addinn.dev.domain.entity.group

import com.google.gson.annotations.SerializedName

data class GroupRequest(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("members") val members: List<String>,
)