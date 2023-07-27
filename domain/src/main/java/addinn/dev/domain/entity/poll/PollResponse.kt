package addinn.dev.domain.entity.poll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PollResponse(
    var id: String?= null,
    var question: String?= null,
    var choice1: String?= null,
    var choice2: String?= null,
    var choice3: String?= null,
    var choiceVote1: String?= null,
    var choiceVote2: String?= null,
    var choiceVote3: String?= null,
    val expirationTime: Long? = null
) : Parcelable

