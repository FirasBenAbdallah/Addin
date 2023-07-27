package addinn.dev.team.presentation.channels

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.chatWidgets.ChannelNameBar
import addinn.dev.team.utils.widgets.chatWidgets.Messages
import addinn.dev.team.utils.widgets.chatWidgets.UserInput
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.group.GroupMessagesViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PrivateGroupsChatView(
    navigator: NavigationProvider,
    usersCount: Int,
    groupId: String,
    viewModel: GroupMessagesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val currentUser = sharedViewModel.getUser()

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    // VIEW MODEL
    val msgState = viewModel.allMessagesState.collectAsState()
    val loadingMsgState = viewModel.loadingMsgState.collectAsState()

    val sendState = viewModel.messageState.collectAsState()

    var msgs by remember { mutableStateOf(listOf<Message>()) }

    // INIT
    LaunchedEffect(Unit) {
        viewModel.getAllMessages(groupId)
    }

    LaunchedEffect(msgState.value) {
        when (msgState.value) {
            is Response.Error -> {
                //val error = (msgState.value as Response.Error).error
            }

            is Response.Success -> {
                msgs = (msgState.value as Response.Success).data
            }

            else -> {}
        }
    }

    LaunchedEffect(sendState.value) {
        when (sendState.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                //msgViewModel.removeIsTyping(chatId, senderId)
            }

            else -> {}
        }
    }

    if (loadingMsgState.value) {
        DialogBoxLoading()
    }

    Scaffold(modifier = Modifier
        .navigationBarsPadding()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            ChannelNameBar(
                channelName = "",
                channelMembers = usersCount,
                scrollBehavior = scrollBehavior,
                onNavIconPressed = {
                    navigator.navigateBack()
                })
        }) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Messages(
                messages = msgs,
                modifier = Modifier.weight(1f),
                scrollState = scrollState,
                currentUserId = currentUser.username!!,
            )

            UserInput(
                onMessageSent = { messageContent ->
                    val messageRequest = MessageRequest(
                        sender = currentUser.username!!,
                        message = messageContent
                    )

                    viewModel.sendMessage(groupId, messageRequest)

                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                isTyping = { _, _ ->

                },
            )
        }
    }

}