package addinn.dev.team.presentation.chat

import addinn.dev.team.utils.widgets.AnimatedTopChatBar
import addinn.dev.team.utils.widgets.ChatInput
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MessagesView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .wrapContentHeight()
            .imePadding()
    ) {
        AnimatedTopChatBar()
        Spacer(modifier = Modifier.weight(1f))
        ChatInput(
            onMessageChange = { messageContent ->
                println("messageContent: $messageContent")
            },
        )
    }
}