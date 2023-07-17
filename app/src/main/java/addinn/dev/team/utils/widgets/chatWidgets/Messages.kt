package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.domain.entity.chat.Message
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Messages(
    messages: List<Message>,
    scrollState: LazyListState,
    currentUserId:String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.sender
                val nextAuthor = messages.getOrNull(index + 1)?.sender
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.sender!!
                val isLastMessageByAuthor = nextAuthor != content.sender!!

                item {
                    Message(
                        msg = content,
                        isUserMe = content.sender!! == currentUserId,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
    }
}