package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.staticModels.ConversationChat
import addinn.dev.team.utils.staticModels.MessageStatus
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ConversationItem(navigator: NavigationProvider, message: ConversationChat) {
    Box(modifier = Modifier.clickable {
        // TODO: ADD NAVIGATION
        navigator.navigateToChat()
    }) {
        Row(
            modifier = Modifier.padding(
                horizontal = 12.dp, vertical = 12.dp
            ), verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(70.dp), shape = CircleShape
            ) {
                Box(
                    modifier = Modifier.background(
                        color = Color.LightGray.copy(0.3f),
                        shape = CircleShape
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // TODO: CONDITION HERE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = message.username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (message.amILastSender) {
                        Text(
                            text = "You: ${message.lastMessage}",
                            style = TextStyle(
                                fontWeight = FontWeight.Light
                            )
                        )
                    } else {
                        Text(
                            text = message.lastMessage,
                            style = TextStyle(
                                fontWeight = FontWeight.Light
                            )
                        )
                    }

                }
                Column {
                    Text(
                        text = message.date,
                        fontWeight = FontWeight.ExtraLight,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    when (message.status) {
                        MessageStatus.RECEIVED -> {
                            MessageStatusIcon(
                                iconId = R.drawable.baseline_check_circle_24,
                                iconColor = Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        MessageStatus.READ -> {
                            MessageStatusIcon(
                                iconId = R.drawable.baseline_check_24,
                                iconColor = Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        MessageStatus.SENT -> {
                            MessageStatusIcon(
                                iconId = R.drawable.baseline_check_circle_outline_24,
                                iconColor = Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        MessageStatus.ERROR -> {
                            MessageStatusIcon(
                                iconId = R.drawable.outline_error_24,
                                iconColor = Color.Red,
                                modifier = Modifier
                                    .size(20.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun MessageStatusIcon(modifier: Modifier = Modifier, iconId: Int, iconColor: Color) {
    Icon(
        painter = painterResource(id = iconId),
        tint = iconColor,
        contentDescription = null,
        modifier = modifier
    )
}

