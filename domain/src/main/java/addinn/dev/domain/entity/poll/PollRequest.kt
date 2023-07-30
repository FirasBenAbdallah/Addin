package addinn.dev.domain.entity.poll

import com.google.gson.annotations.SerializedName

data class PollRequest(
    @SerializedName("question") val question: String,
    @SerializedName("choice1") val choice1: String,
    @SerializedName("choice2") val choice2: String,
    @SerializedName("choice3") val choice3: String,
    @SerializedName("choiceVote1") val choiceVote1: Int,
    @SerializedName("choiceVote2") val choiceVote2: Int,
    @SerializedName("choiceVote3") val choiceVote3: Int,
    @SerializedName("totalVotes") val totalVotes: Int = 0,
    @SerializedName("votesBy") val votesBy: List<String> = emptyList(),
)