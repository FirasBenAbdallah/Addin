package addinn.dev.team.presentation.chat

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.ConversationItem
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatView(navigator: NavigationProvider) {

    val columnScrollState = rememberLazyListState()
    val rowScrollState = rememberLazyListState()

    Scaffold {
        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp)
                .fillMaxSize()
        ) {
            val (welcomeText, usersRow, divider, chatList) = createRefs()

            Text(
                modifier = Modifier.constrainAs(welcomeText) {
                    top.linkTo(parent.top, margin = 50.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }, text = "Hello, username", style = TextStyle(
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            )

            Box(modifier = Modifier.constrainAs(usersRow) {
                top.linkTo(welcomeText.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                LazyRow(modifier = Modifier.fillMaxWidth(), state = rowScrollState) {
                    items(10) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.LightGray.copy(alpha = 0.5f), shape = CircleShape)
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

            Divider(
                modifier = Modifier.constrainAs(divider) {
                    top.linkTo(usersRow.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = fillToConstraints
                }, color = Color.LightGray.copy(alpha = 0.2f)
            )

            Box(modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(divider.bottom)
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
                    items(4) {
                        ConversationItem(navigator = navigator)
                    }
                }
            }
        }
    }
}
