package addinn.dev.team.presentation.channels

import addinn.dev.team.utils.navigation.NavigationProvider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ChannelsView(navigator: NavigationProvider) {
    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 6.dp)
            .fillMaxSize()
    ) {
        val (channelName, optionsList) = createRefs()

        Text(
            modifier = Modifier.constrainAs(channelName) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }, text = "Mobile dev Channel", style = TextStyle(
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        )

        Box(modifier = Modifier.constrainAs(optionsList) {
            top.linkTo(channelName.bottom, margin = 16.dp)
            start.linkTo(parent.start, margin = 16.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.2f)
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(16.dp)
                        )

                        Text(text = "Conversation", style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W300
                        ))

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Arrow",
                                modifier = Modifier.padding(6.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(12.dp)
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.2f)
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(16.dp)
                        )

                        Text(text = "Files", style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W300
                        ))

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Arrow",
                                modifier = Modifier.padding(6.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(12.dp)
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.2f)
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(16.dp)
                        )

                        Text(text = "Images", style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W300
                        ))

                        Spacer(modifier = Modifier.weight(1f))

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Arrow",
                                modifier = Modifier.padding(6.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(12.dp)
                            )
                        }

                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.2f)
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(16.dp)
                        )

                        Text(text = "View members", style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W300
                        ))

                        Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Arrow",
                                modifier = Modifier.padding(6.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(12.dp)
                            )


                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.LightGray.copy(alpha = 0.2f)
                        )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .padding(8.dp)
                                .background(color = Color.White, shape = CircleShape)
                                .padding(16.dp)
                        )

                        Text(text = "Create a poll", style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W300
                        ))

                        Spacer(modifier = Modifier.weight(1f))

                            Icon(
                                imageVector = Icons.Outlined.ArrowForward,
                                contentDescription = "Arrow",
                                modifier = Modifier
                                    .padding(6.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .padding(12.dp)
                            )


                    }
                }
            }
        }

    }
}