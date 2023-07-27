package addinn.dev.team.presentation.channels

import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.group.Group
import addinn.dev.domain.entity.group.GroupRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.team.presentation.chat.UserCheckItem
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.chat.SearchUsersViewModel
import addinn.dev.team.viewModel.group.CreateGroupViewModel
import addinn.dev.team.viewModel.group.GetGroupsViewModel
import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PrivateGroupsView(
    navigator: NavigationProvider,
    searchUsersViewModel: SearchUsersViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel(),
    getGroupsViewModel: GetGroupsViewModel = hiltViewModel(),
    createGroupViewModel: CreateGroupViewModel = hiltViewModel()
) {
    val groupNameField = remember { mutableStateOf("") }
    val searchField = remember { mutableStateOf("") }
    val messageField = remember { mutableStateOf("") }

    val lazyGridState = rememberLazyGridState()
    val columnScrollState = rememberLazyListState()

    val showDialog = remember { mutableStateOf(false) }

    val users = remember { mutableStateOf(emptyList<User>()) }
    val selectedUsers = remember { mutableStateOf(emptyList<String>()) }

    val groups = remember { mutableStateOf(emptyList<Group>()) }

    val currentUser = sharedViewModel.getUser()
    val username = currentUser.username!!

    val requestState = searchUsersViewModel.requestState.collectAsState()
    val createState = createGroupViewModel.create.collectAsState()

    val loadingGrp = getGroupsViewModel.loadingState.collectAsState()
    val loadingCreate = createGroupViewModel.loadingState.collectAsState()
    val reqGrps = getGroupsViewModel.groupsList.collectAsState()

    if (loadingGrp.value ||loadingCreate.value) {
        DialogBoxLoading()
    }

    LaunchedEffect(Unit) {
        getGroupsViewModel.getGroups(username)
    }

    LaunchedEffect(reqGrps.value) {
        when (reqGrps.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                val data = (reqGrps.value as Response.Success).data
                groups.value = data
            }

            else -> {}
        }

    }

    if (searchField.value.isEmpty()) {
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

    LaunchedEffect(createState.value) {
        when (createState.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                val id = (createState.value as Response.Success).data
                navigator.navigateToPrivateGroupsChat(usersCount = selectedUsers.value.size+1, groupId = id)
            }

            else -> {}
        }
    }

    if (showDialog.value) {

        Dialog(onDismissRequest = { showDialog.value = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                    ) {
                        OutlinedTextField(value = groupNameField.value, onValueChange = {
                            groupNameField.value = it
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), label = {
                            Text(text = "Group Name")
                        })

                        OutlinedTextField(value = searchField.value, onValueChange = {
                            searchField.value = it
                            searchUsersViewModel.search(username, it)
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), label = {
                            Text(text = "Search")
                        })

                        LazyColumn(
                            state = columnScrollState, modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            items(users.value) { user ->
                                UserCheckItem(
                                    user = user,
                                    isChecked = selectedUsers.value.contains(user.username!!),
                                ) {
                                    if (selectedUsers.value.contains(user.username!!)) {
                                        selectedUsers.value =
                                            selectedUsers.value.filter { it != user.username!! }
                                    } else {
                                        selectedUsers.value = selectedUsers.value + user.username!!
                                    }
                                }
                            }
                        }

                        OutlinedTextField(value = messageField.value, onValueChange = {
                            messageField.value = it
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp), label = {
                            Text(text = "Message")
                        })

                        OutlinedButton(onClick = {
                            val members = selectedUsers.value + username
                            val groupRequest = GroupRequest(id= System.currentTimeMillis().toString(), name = groupNameField.value, members = members)
                            val messageRequest = MessageRequest(sender = username, message = messageField.value)
                            createGroupViewModel.createGroup(groupRequest = groupRequest, messageRequest = messageRequest)
                        }) {
                            Text(text = "Create Group")
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Groups"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Add group"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "add grp")
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                reverseLayout = false,
                state = lazyGridState,
                userScrollEnabled = true
            ) {
                items(groups.value) {
                    GroupCardItem(group = it, navigator = navigator)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupCardItem(group: Group, navigator: NavigationProvider) {

    val members = group.members!!.take(3)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        onClick = {
            navigator.navigateToPrivateGroupsChat(
                usersCount = group.members!!.count(),
                groupId = group.id!!
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.padding(16.dp)) {
                members.forEachIndexed { index, drawable ->
                    val imageUrl =
                        "https://firebasestorage.googleapis.com/v0/b/team-addinn.appspot.com/o/avatars%2F${drawable}.png?alt=media"
                    val offset by animateDpAsState(
                        targetValue = (index * 30).dp,
                    )
                    val modifier = Modifier
                        .size(70.dp)
                        .absoluteOffset(x = offset)

                    Card(
                        modifier = modifier,
                        shape = CircleShape,
                        border = BorderStroke(
                            width = 2.dp,
                            color = Color.White
                        ),
                    ) {
                        AsyncImage(
                            model = Uri.parse(imageUrl),
                            contentDescription = "user image",
                        )
                    }

                }
            }
            Text(
                text = group.name!!,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

        }
    }
}