package addinn.dev.team.presentation.chat

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.staticModels.fakeConversationData
import addinn.dev.team.utils.widgets.chatWidgets.ConversationItem
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatView(navigator: NavigationProvider, modifier: Modifier = Modifier) {

    val columnScrollState = rememberLazyListState()
    val rowScrollState = rememberLazyListState()

    val searchText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Scaffold(modifier = modifier, floatingActionButton = {
        FloatingActionButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Outlined.AddCircle, contentDescription = "new chat")
        }
    })
    {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxSize()
        ) {
            val (messagesRow, searchField, usersRow, chatList) = createRefs()

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

            Box(
                modifier = Modifier
                    .constrainAs(searchField) {
                        top.linkTo(messagesRow.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .shadow(0.dp, shape = RoundedCornerShape(16.dp), clip = true)
            ) {
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Enter)
                    }),
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

            Box(modifier = Modifier.constrainAs(usersRow) {
                top.linkTo(searchField.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                LazyRow(modifier = Modifier.fillMaxWidth(), state = rowScrollState) {
                    items(10) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(
                                    color = Color.LightGray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .size(60.dp)
                        ) {
                            Image(
                                painter = painterResource(
                                    id = R.drawable.avatar,
                                ),
                                contentDescription = "avatar"
                            )
                        }
                    }

                }
            }

            Box(modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(usersRow.bottom)
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
                    items(fakeConversationData) { message ->
                        ConversationItem(navigator = navigator, message = message)
                    }
                }
            }
        }
    }
}
