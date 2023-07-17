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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun MembersView(
    navigator: NavigationProvider,
    getUsersViewModel: GetUsersViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {

    // CURRENT USER
    val currentUser = sharedViewModel.getUser()

    // VIEW MODEL
    val requestState = getUsersViewModel.usersState.collectAsState()
    val loadingState = getUsersViewModel.loadingState.collectAsState()

    val users = remember { mutableStateOf(emptyList<User>()) }

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

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                items(users.value) { user ->
                    MemberItemCard(user = user, action = {
                        navigator.navigateToChat(
                            senderId = currentUser.username!!,
                            receiverId = user.username!!
                        )
                    })
                }
            }
        }
    }
}

@Composable
fun MemberItemCard(user: User, action: () -> Unit) {
    val snooze = SwipeAction(
        icon = { Text("Chat") },
        background = Color.Yellow,
        isUndo = true,
        onSwipe = action,
    )

    SwipeableActionsBox(
        endActions = listOf(snooze)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = { action() }),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                val image: Painter = painterResource(id = R.drawable.avatar)
                Image(
                    modifier = Modifier
                        .size(80.dp, 80.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    painter = image,
                    alignment = Alignment.CenterStart,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = user.email!!,
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "info | more info",
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = typography.bodyMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Blue.copy(.08f))
                    ) {
                        Text(
                            text = "info", modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
                            style = typography.bodySmall,
                            color = Color.Blue
                        )
                    }
                }
            }
        }
    }
}