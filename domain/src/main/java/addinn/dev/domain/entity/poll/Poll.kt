package addinn.dev.domain.entity.poll

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Poll(
    var id: String? = null,
    var question: String?= null,
    var choice1: String?= null,
    var choice2: String?= null,
    var choice3: String?= null,
): Parcelable
