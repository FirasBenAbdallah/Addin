package addinn.dev.team.presentation.chat

import addinn.dev.team.R
import addinn.dev.team.utils.staticModels.fakeMessagesData
import addinn.dev.team.utils.widgets.chatWidgets.AnimatedTopChatBar
import addinn.dev.team.utils.widgets.chatWidgets.ChatInput
import addinn.dev.team.utils.widgets.chatWidgets.MyMessageItem
import addinn.dev.team.utils.widgets.chatWidgets.OtherMessageItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.imePadding
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MessagesView() {

    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = fakeMessagesData.size)
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

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
                        MyMessageItem(message = message, onLongPress = {
                            println("hello bototm sheet")
                            openBottomSheet = true
                        })
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

    if (openBottomSheet) {
        val windowInsets = if (edgeToEdgeEnabled)
            WindowInsets(0) else BottomSheetDefaults.windowInsets

        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = windowInsets,
        ) {
            Text("Bottom Sheet")
        }
    }
}

@Composable
fun MessageModalView() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.like_icon),
                    contentDescription = "like_icon",
                    modifier = Modifier.aspectRatio(1f).padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.love_icon),
                    contentDescription = "love_icon",
                    modifier = Modifier.aspectRatio(1f).padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clap_icon),
                    contentDescription = "clap_icon",
                    modifier = Modifier.aspectRatio(1f).padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.funny_icon),
                    contentDescription = "funny_icon",
                    modifier = Modifier.aspectRatio(1f).padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray.copy(alpha = 0.3f), shape = CircleShape)
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sad_icon),
                    contentDescription = "sad_icon",
                    modifier = Modifier.aspectRatio(1f).padding(8.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Text("hello")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ModalPreview() {
    MessageModalView()
}