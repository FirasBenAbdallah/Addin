package addinn.dev.team.presentation.channels

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.chat.GetUsersViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsView(
    navigator: NavigationProvider,
    modifier: Modifier = Modifier,
    getUsersViewModel: GetUsersViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    // CURRENT USER
    val currentUser = sharedViewModel.getUser()

    // VIEW MODEL
    val requestState = getUsersViewModel.usersState.collectAsState()
    val loadingState = getUsersViewModel.loadingState.collectAsState()

    val users = remember { mutableStateOf(emptyList<User>()) }

    val channelImage = when (currentUser.department) {
        "Mobile" -> R.drawable.mobile_pic
        "Web" -> R.drawable.web
        "Administration" -> R.drawable.admin
        "Data" -> R.drawable.data
        "DevOps" -> R.drawable.cloud
        else -> R.drawable.mobile_pic
    }

    LaunchedEffect(Unit) {
        getUsersViewModel.getUsersByDep(currentUser.id!!, currentUser.department!!)
    }

    LaunchedEffect(requestState.value) {
        when (requestState.value) {
            is Response.Error -> {
            }

            is Response.Success -> {
                val data = (requestState.value as Response.Success).data
                users.value = data
            }

            else -> {}
        }
    }

    if (loadingState.value) {
        DialogBoxLoading()
    }

    val data = listOf(
        ChannelItem(
            name = "Main Channel",
            image = R.drawable.conversation_ill,
            colors = listOf(
                Color(0xffBE93C5),
                Color(0xff7BC6CC),
            ),
            onClick = { navigator.navigateToGroupChat(users.value.count()) }),
        ChannelItem(
            name = "Shared Files",
            image = R.drawable.files_ill,
            colors = listOf(
                Color(0xff74ebd5),
                Color(0xffACB6E5),
            ),
            onClick = {}),
        ChannelItem(
            name = "Members",
            image = R.drawable.members_ill,
            colors = listOf(
                Color(0xffB799FF),
                Color(0xffACBCFF),
            ),
            onClick = {
                navigator.navigateToMembers()
            }),
    )

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(
            title = {
                Text(text = "${currentUser.department!!} Channel")
            },
            actions = {
                IconButton(onClick = { navigator.navigateToPrivateGroups() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_groups_24),
                        contentDescription = "my grps"
                    )
                }
            })
    }) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${currentUser.department!!} Channel", style = TextStyle(
                    fontWeight = FontWeight.W600,
                    fontSize = 28.sp,
                    letterSpacing = 1.25.sp
                )
            )

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .background(color = Color.LightGray, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(id = channelImage),
                    contentDescription = "Mobile dev Channel",
                )
            }

            Text(
                text = users.value.count().toString(), style = TextStyle(
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                items(data) { item ->
                    ElevatedCard(
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color.Transparent
                        ),
                        onClick = item.onClick
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = item.colors
                                    )
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = item.image),
                                contentDescription = item.name,
                                modifier = Modifier.padding(18.dp)
                            )
                            Text(
                                text = item.name,
                                modifier = Modifier.padding(12.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }


        }
    }
}

class ChannelItem(
    var name: String,
    var image: Int,
    var colors: List<Color>,
    var onClick: () -> Unit
)