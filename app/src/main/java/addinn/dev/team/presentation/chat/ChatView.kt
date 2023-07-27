package addinn.dev.team.presentation.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.chatWidgets.ConversationItem
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.chat.ConversationViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatView(
    navigator: NavigationProvider,
    modifier: Modifier = Modifier,
    conversationViewModel: ConversationViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel() ////
) {
    val columnScrollState = rememberLazyListState()

    // VIEW MODEL
    val conversationState = conversationViewModel.requestState.collectAsState()
    val convLoadState = conversationViewModel.loadingState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val conversations = remember { mutableStateOf(emptyList<Chat>()) }

    // CURRENT USER
    val currentUser = sharedViewModel.getUser() /////

    LaunchedEffect(Unit) {
        conversationViewModel.getLastestMessages(currentUser.username!!)
    }

    LaunchedEffect(conversationState.value) {
        when (conversationState.value) {
            is Response.Error -> {
                val error = (conversationState.value as Response.Error).error
                snackbarHostState.showSnackbar(error)
            }

            is Response.Success -> {
                val data = (conversationState.value as Response.Success).data
                conversations.value = data
            }

            else -> {}
        }
    }

    if (convLoadState.value) {
        DialogBoxLoading()
    }

    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = { navigator.navigateToNewMessage() }) {
            Icon(Icons.Outlined.AddCircle, contentDescription = "new chat")
        }
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) })
    {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxSize()
        ) {
            val (messagesRow, chatList) = createRefs()

            Row(
                modifier = Modifier.constrainAs(messagesRow) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    width = fillToConstraints
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Messages",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.W300,
                        color = Color.Black,
                        letterSpacing = 1.2.sp
                    )
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Box(
                        modifier = Modifier.background(
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.avatar),
                            contentDescription = "avatar"
                        )
                    }
                }
            }

            Box(modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(messagesRow.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = fillToConstraints
                    height = fillToConstraints
                }) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = columnScrollState,
                ) {
                    items(conversations.value) { chat ->
                        ConversationItem(navigate = {
                            if ((chat.lastMessageSenderId != currentUser.username!!) && (chat.lastMessageStatus != "SEEN")) {
                                conversationViewModel.setMessageSeen(chat.id!!)
                            }

                            navigator.navigateToChat(
                                senderId = currentUser.username!!,
                                receiverId = if (chat.participant1 == currentUser.username!!) chat.participant2!! else chat.participant1!!
                            )
                        }, message = chat, uid = currentUser.username!!)
                    }
                }
            }
        }
    }
}
