package addinn.dev.team.presentation.chat

import addinn.dev.team.utils.staticModels.fakeMessagesData
import addinn.dev.team.utils.widgets.chatWidgets.AnimatedTopChatBar
import addinn.dev.team.utils.widgets.chatWidgets.ChatInput
import addinn.dev.team.utils.widgets.chatWidgets.MyMessageItem
import addinn.dev.team.utils.widgets.chatWidgets.OtherMessageItem
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.imePadding
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun MessagesView() {

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = fakeMessagesData.size)
// TODO : SCROLL TO LAST ITEM
    LaunchedEffect(Unit) {
        scrollState.animateScrollToItem(fakeMessagesData.size)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .wrapContentHeight()
            .imePadding()
    ) {
        AnimatedTopChatBar()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = scrollState
        ) {
            items(fakeMessagesData) { message ->
                when (message.isMine) {
                    true -> {
                        MyMessageItem(message = message)
                    }

                    false -> {
                        OtherMessageItem(message = message)
                    }
                }
            }
        }
        ChatInput(
            onMessageChange = { messageContent ->
                println("messageContent: $messageContent")
            },
        )
    }
}