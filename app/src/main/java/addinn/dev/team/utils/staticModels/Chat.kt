package addinn.dev.team.utils.staticModels

enum class MessageStatus {
    RECEIVED, READ
}

data class ChatRowData(
    var text: String = "",
    var textWidth: Int = 0,
    var lastLineWidth: Float = 0f,
    var lineCount: Int = 0,
    var rowWidth: Int = 0,
    var rowHeight: Int = 0,
    var parentWidth: Int = 0,
    var measuredType: Int = 0,
) {

    override fun toString(): String {
        return "ChatRowData text: $text, " +
                "lastLineWidth: $lastLineWidth, lineCount: $lineCount, " +
                "textWidth: ${textWidth}, rowWidth: $rowWidth, height: $rowHeight, " +
                "parentWidth: $parentWidth, measuredType: $measuredType"
    }
}

data class ChatMessage(
    val profileUUID: String = "",
    var message: String = "",
    var date: String = "",
    var status: MessageStatus,
    var isMine: Boolean = false
)

val fakeData = listOf(
    ChatMessage(
        profileUUID = "1",
        message = "Hello!",
        date = "12:00",
        status = MessageStatus.READ,
        isMine = true
    ),
    ChatMessage(
        profileUUID = "2",
        message = "Hi there!",
        date = "12:01",
        status = MessageStatus.READ,
        isMine = false
    ),
    ChatMessage(
        profileUUID = "1",
        message = "How are you?",
        date = "12:01",
        status = MessageStatus.READ,
        isMine = true
    ),
    ChatMessage(
        profileUUID = "2",
        message = "I'm doing well, thanks!",
        date = "12:02",
        status = MessageStatus.READ,
        isMine = false
    ),
    ChatMessage(
        profileUUID = "1",
        message = "That's great!",
        date = "12:03",
        status = MessageStatus.READ,
        isMine = true
    ),
    ChatMessage(
        profileUUID = "1",
        message = "What are your plans for the weekend?",
        date = "12:03",
        status = MessageStatus.READ,
        isMine = true
    ),
    ChatMessage(
        profileUUID = "2",
        message = "I'm going to a concert on Saturday!",
        date = "12:03",
        status = MessageStatus.READ,
        isMine = false
    ),
    ChatMessage(
        profileUUID = "1",
        message = "That sounds amazing! Which band are you going to see?",
        date = "12:04",
        status = MessageStatus.READ,
        isMine = true
    ),
    ChatMessage(
        profileUUID = "2",
        message = "I'm going to see my favorite band, XYZ.",
        date = "12:05",
        status = MessageStatus.READ,
        isMine = false
    ),
    ChatMessage(
        profileUUID = "1",
        message = "I've heard great things about them. Enjoy the concert!",
        date = "12:05",
        status = MessageStatus.RECEIVED,
        isMine = true
    )
)