package addinn.dev.team.utils.functions

import addinn.dev.team.utils.staticModels.ChatRowData
import androidx.compose.ui.layout.Placeable

fun calculateChatWidthAndHeight(
    chatRowData: ChatRowData,
    message: Placeable,
    status: Placeable?,
) {

    if (status != null) {

        val lineCount = chatRowData.lineCount
        val lastLineWidth = chatRowData.lastLineWidth
        val parentWidth = chatRowData.parentWidth

        val padding = (message.measuredWidth - chatRowData.textWidth) / 2
        if (lineCount > 1 && lastLineWidth + status.measuredWidth >= chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 0
        } else if (lineCount > 1 && lastLineWidth + status.measuredWidth < chatRowData.textWidth + padding) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 1
        } else if (lineCount == 1 && message.width + status.measuredWidth >= parentWidth) {
            chatRowData.rowWidth = message.measuredWidth
            chatRowData.rowHeight = message.measuredHeight + status.measuredHeight
            chatRowData.measuredType = 2
        } else {
            chatRowData.rowWidth = message.measuredWidth + status.measuredWidth
            chatRowData.rowHeight = message.measuredHeight
            chatRowData.measuredType = 3
        }
    } else {
        chatRowData.rowWidth = message.width
        chatRowData.rowHeight = message.height
    }
}