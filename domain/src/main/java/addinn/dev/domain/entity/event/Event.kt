package addinn.dev.domain.entity.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    var id: String? = null,
    var title: String? = null,
    var description: String? = null,
    var type: String? = null,
    var date: Long? = null,
    var time: Long? = null,
) : Parcelable