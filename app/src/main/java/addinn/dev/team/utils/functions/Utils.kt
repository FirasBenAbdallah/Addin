package addinn.dev.team.utils.functions

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun getDate(time: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time

    val currentTime = Calendar.getInstance()

    val isSameDay = calendar.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

    return when {
        isSameDay -> timeFormat.format(calendar.time)
        isYesterday(calendar, currentTime) -> "Yest. ${timeFormat.format(calendar.time)}"
        calendar.get(Calendar.YEAR) != currentTime.get(Calendar.YEAR) -> dateFormat.format(calendar.time)
        else -> "${getDayOfWeekAbbreviation(calendar.get(Calendar.DAY_OF_WEEK))} ${timeFormat.format(calendar.time)}"
    }
}

fun isYesterday(calendar: Calendar, currentTime: Calendar): Boolean {
    currentTime.add(Calendar.DAY_OF_YEAR, -1)
    val isYesterday = calendar.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR) &&
            calendar.get(Calendar.DAY_OF_YEAR) == currentTime.get(Calendar.DAY_OF_YEAR)
    currentTime.add(Calendar.DAY_OF_YEAR, 1) // Reset the current time back
    return isYesterday
}

fun getDayOfWeekAbbreviation(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        Calendar.MONDAY -> "Mon."
        Calendar.TUESDAY -> "Tues."
        Calendar.WEDNESDAY -> "Wed."
        Calendar.THURSDAY -> "Thurs."
        Calendar.FRIDAY -> "Fri."
        Calendar.SATURDAY -> "Sat."
        Calendar.SUNDAY -> "Sun."
        else -> ""
    }
}

fun createChatRoomId(senderId: String, receiverId: String): String {
    return if (senderId.compareTo(receiverId) <= -1) {
        receiverId + senderId
    } else {
        senderId + receiverId
    }
}

fun getLastSeenTime(lastSeenTimestamp: Long): String {
    val currentTime = Calendar.getInstance().timeInMillis
    val timeDifference = currentTime - lastSeenTimestamp

    val minutes = timeDifference / (1000 * 60)
    val hours = timeDifference / (1000 * 60 * 60)
    val days = timeDifference / (1000 * 60 * 60 * 24)

    return when {
        minutes < 60 -> "$minutes min ago"
        hours < 24 -> "$hours hours ago"
        days < 30 -> "$days days ago"
        else -> {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = lastSeenTimestamp
            val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            dateFormat.format(calendar.time)
        }
    }
}
