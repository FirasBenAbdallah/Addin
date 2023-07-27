package addinn.dev.domain.entity.group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
    var id : String? = null,
    var name : String? = null,
    var members : List<String>? = null,
) : Parcelable