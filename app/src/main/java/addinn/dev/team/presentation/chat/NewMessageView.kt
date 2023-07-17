package addinn.dev.team.presentation.chat

import addinn.dev.data.model.MessageNotificationData
import addinn.dev.data.model.PushNotification
import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.team.utils.functions.createChatRoomId
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.chat.MessagesViewModel
import addinn.dev.team.viewModel.chat.SearchUsersViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun NewMessageView(
    navigator: NavigationProvider,
    searchUsersViewModel: SearchUsersViewModel = hiltViewModel(),
    msgViewModel: MessagesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    val searchText = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val selectedUser = remember { mutableStateOf(User()) }
    val columnScrollState = rememberLazyListState()

    // send message
    val sendState = msgViewModel.messageState.collectAsState()

    /***/
    val currentUser = sharedViewModel.getUser()
    val username = currentUser.username!!

    // VIEW MODEL
    val requestState = searchUsersViewModel.requestState.collectAsState()

    val users = remember { mutableStateOf(emptyList<User>()) }

    if (searchText.value.isEmpty()) {
        users.value = emptyList()
    }

    LaunchedEffect(requestState.value) {
        when (requestState.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                val data = (requestState.value as Response.Success).data
                users.value = emptyList()
                users.value = data
                //remove current user from list
                users.value = users.value.filter { it.username!! != username }
            }

            else -> {}
        }
    }

    LaunchedEffect(sendState.value) {
        when (sendState.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                navigator.navigateToChat(senderId = username, receiverId = selectedUser.value.username!!)
            }

            else -> {}
        }
    }

    Scaffold(
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("New message") },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(16.dp), clip = true)
            ) {
                TextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                        searchUsersViewModel.search(username, it)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = "search",
                            tint = Color.Gray
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Search ...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

            LazyColumn(
                state = columnScrollState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(users.value) { user ->
                    UserCheckItem(
                        user = user,
                        isChecked = selectedUser.value == user
                    ) {
                        if (it) {
                            selectedUser.value = user
                        } else {
                            selectedUser.value = User()
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(16.dp), clip = true)
            ) {
                TextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.PlayArrow,
                            contentDescription = "type",
                            tint = Color.Gray
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Type ...",
                            style = TextStyle(color = Color.LightGray)
                        )
                    }
                )
            }

            OutlinedButton(
                onClick = {
                    val messageRequest = MessageRequest(
                        sender = username,
                        message = message.value
                    )
                    val chatRequest = ChatRequest(
                        participant1 = username,
                        participant2 = selectedUser.value.username!!,
                        lastMessageSenderId = username,
                        lastMessage = message.value,
                        id = createChatRoomId(username, selectedUser.value.username!!)
                    )
                    msgViewModel.sendMessage(chatRequest, messageRequest)

                    // SEND PUSH NOTIFICATION
                    val topic = "/topics/${selectedUser.value.username!!}"

                    PushNotification(
                        data = MessageNotificationData(
                            title = username,
                            message = message.value,
                            fromId = username
                        ),
                        to = topic
                    ).also { notification ->
                        msgViewModel.postNotification(notification)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = selectedUser.value.id != null
            ) {
                Text(text = "Send")
            }
        }
    }
}

@Composable
fun UserCheckItem(user: User, isChecked: Boolean, onCheckChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {

        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onCheckChange(it)
            },
            enabled = true,
            modifier = Modifier.shadow(0.dp, shape = RoundedCornerShape(16.dp), clip = true),
        )
        Text(text = user.email!!, modifier = Modifier.padding(8.dp))
    }
}
