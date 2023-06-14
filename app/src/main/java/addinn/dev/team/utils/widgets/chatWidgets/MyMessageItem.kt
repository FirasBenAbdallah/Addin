package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.team.utils.staticModels.ChatMessage
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MyMessageItem(message: ChatMessage) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                start = 20.dp,
                end = 12.dp,
                top = 8.dp,
                bottom = 8.dp
            )
    ) {
        // This is chat bubble

        ChatBubbleConstraints(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clickable { },
            content = {
                TextMessageInsideBubble(
                    modifier = Modifier.padding(
                        start = 12.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    ),
                    text = message.message,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    messageStat = {
                        MessageTimeText(
                            modifier = Modifier.wrapContentSize(),
                            messageTime = message.date,
                            messageStatus = message.status
                        )
                    }
                )
            }
        )
    }
}