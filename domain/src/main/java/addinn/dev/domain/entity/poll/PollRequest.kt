package addinn.dev.domain.entity.poll

import com.google.gson.annotations.SerializedName

data class PollRequest(
    @SerializedName("question") val question: String,
    @SerializedName("choice1") val choice1: String,
    @SerializedName("choice2") val choice2: String,
    @SerializedName("choice3") val choice3: String,
)