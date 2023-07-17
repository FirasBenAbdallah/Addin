package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.team.R
import addinn.dev.team.utils.functions.getDate
import android.net.Uri
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ConversationItem(navigate: () -> Unit, message: Chat, uid: String) {
    // DATE
    val time = getDate(message.lastMessageDate!!)
    val receiverUsername = if (message.participant1 == uid) {
        message.participant2!!
    } else {
        message.participant1!!
    }

    val imageUrl =
        "https://firebasestorage.googleapis.com/v0/b/team-addinn.appspot.com/o/avatars%2F${receiverUsername}.png?alt=media"

    Box(modifier = Modifier.clickable {
        navigate()
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
                    AsyncImage(
                        model = Uri.parse(imageUrl),
                        contentDescription = "avatar",
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Column {
                        Text(
                            text = receiverUsername,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (message.participant1 != uid) {
                                message.lastMessage!!
                            } else {
                                "You: ${message.lastMessage!!}"
                            },
                            style = TextStyle(
                                fontWeight = FontWeight.Light
                            )
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = time,
                        maxLines = 1,
                        fontWeight = FontWeight.ExtraLight,
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (uid == message.lastMessageSenderId!!) {
                        when (message.lastMessageStatus!!) {
                            "SENT" -> {
                                MessageStatusIcon(
                                    iconId = R.drawable.baseline_check_circle_outline_24,
                                    iconColor = Color.Gray,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }

                            "SEEN" -> {
                                MessageStatusIcon(
                                    iconId = R.drawable.baseline_check_24,
                                    iconColor = Color.Blue,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    } else {
                        when (message.lastMessageStatus!!) {
                            "SENT" -> {
                                MessageStatusIcon(
                                    iconId = R.drawable.baseline_brightness_1_24,
                                    iconColor = Color.Blue,
                                    modifier = Modifier
                                        .size(10.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
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

