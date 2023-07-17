package addinn.dev.team.presentation.chat

import addinn.dev.data.model.MessageNotificationData
import addinn.dev.data.model.PushNotification
import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.team.utils.functions.createChatRoomId
import addinn.dev.team.utils.functions.getLastSeenTime
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.chatWidgets.Messages
import addinn.dev.team.utils.widgets.chatWidgets.UserInput
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.chat.GetUserRealtimeViewModel
import addinn.dev.team.viewModel.chat.MessagesViewModel
import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun MessagesView(
    navigator: NavigationProvider,
    senderUsername: String,
    receiverId: String,
    msgViewModel: MessagesViewModel = hiltViewModel(),
    userInfoViewModel: GetUserRealtimeViewModel = hiltViewModel()
) {
    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    // VIEW MODEL
    val msgState = msgViewModel.allMessagesState.collectAsState()
    val loadingMsgState = msgViewModel.loadingMsgState.collectAsState()

    val userInfo = userInfoViewModel.userInfo.collectAsState()
    var receiver by remember { mutableStateOf(User()) }

    val userStatus = if(receiver.getIsOnline() == null) "offline" else {
        if(receiver.getIsOnline()!!){
            "online"
        }else{
            if (receiver.lastSeen != null) {
                "Last seen : ${getLastSeenTime(receiver.lastSeen!!)}"
            } else {
                "Offline"
            }
        }
    }

    val isTypingState = msgViewModel.isTypingState.collectAsState()

    val sendState = msgViewModel.messageState.collectAsState()

    var msgs by remember { mutableStateOf(listOf<Message>()) }

    var isTypingList by remember { mutableStateOf(listOf<String>()) }
    var isTyping by remember { mutableStateOf(false) }

    isTyping = isTypingList.contains(receiverId)

    val chatId = createChatRoomId(senderUsername, receiverId)

    // DETECT IF USER ON SCREEN
    var isUserOnScreen by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    val imageUrl =
        "https://firebasestorage.googleapis.com/v0/b/team-addinn.appspot.com/o/avatars%2F${receiverId}.png?alt=media"

    DisposableEffect(Unit) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                isUserOnScreen = true
            }

            override fun onPause(owner: LifecycleOwner) {
                isUserOnScreen = false
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    // INIT
    LaunchedEffect(Unit) {
        msgViewModel.getAllMessages(chatId)
        msgViewModel.listenIsTyping(chatId)
        userInfoViewModel.getData(receiverId)
    }

    LaunchedEffect(isTypingState.value) {
        when (isTypingState.value) {
            is Response.Error -> {
                //val error = (msgState.value as Response.Error).error
            }

            is Response.Success -> {
                isTypingList = (isTypingState.value as Response.Success).data
            }

            else -> {}
        }
    }

    LaunchedEffect(userInfo.value) {
        when (userInfo.value) {
            is Response.Error -> {
                //val error = (msgState.value as Response.Error).error
            }

            is Response.Success -> {
                receiver = (userInfo.value as Response.Success).data
            }

            else -> {}
        }
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

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AsyncImage(
                            model = Uri.parse(imageUrl),
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .size(42.dp)
                                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                                .clip(CircleShape)
                                .align(Alignment.Top),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                        Column(verticalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = receiverId,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                modifier = Modifier.padding(start = 8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isTyping) "isTyping" else userStatus.toString(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.W400,
                                    color = Color.LightGray
                                ),
                                modifier = Modifier.padding(start = 8.dp),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateToHome() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    )
    { paddingValues ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Messages(
                messages = msgs,
                modifier = Modifier.weight(1f),
                scrollState = scrollState,
                currentUserId = senderUsername,
            )
            UserInput(
                onMessageSent = { messageContent ->
                    val messageRequest = MessageRequest(
                        sender = senderUsername,
                        message = messageContent
                    )
                    val chatRequest = ChatRequest(
                        participant1 = senderUsername,
                        participant2 = receiverId,
                        lastMessageSenderId = senderUsername,
                        lastMessage = messageContent,
                        id = createChatRoomId(senderUsername, receiverId)
                    )
                    msgViewModel.sendMessage(chatRequest, messageRequest)

                    // SEND PUSH NOTIFICATION
                    val topic = "/topics/${receiverId}"

                    PushNotification(
                        data = MessageNotificationData(
                            title = senderUsername,
                            message = messageContent,
                            fromId = senderUsername
                        ),
                        to = topic
                    ).also { notification ->
                        msgViewModel.postNotification(notification)
                    }

                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                isTyping = { isTyping, _ ->
                    if (isTyping) {
                        msgViewModel.sendIsTyping(chatId, senderUsername)
                    } else {
                        msgViewModel.removeIsTyping(chatId, senderUsername)
                    }
                },
            )
        }
    }
}
