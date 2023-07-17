package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.domain.entity.chat.Message
import addinn.dev.team.utils.functions.getDate
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Message(
    msg: Message, isUserMe: Boolean, isFirstMessageByAuthor: Boolean, isLastMessageByAuthor: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val imageUrl =
        "https://firebasestorage.googleapis.com/v0/b/team-addinn.appspot.com/o/avatars%2F${msg.sender!!}.png?alt=media"

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier
        .padding(top = 8.dp)
        .fillMaxWidth() else Modifier.fillMaxWidth()
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = spaceBetweenAuthors, horizontalArrangement = Arrangement.End) {
            if (isLastMessageByAuthor && !isUserMe) {
                // Avatar
                AsyncImage(
                    model = Uri.parse(imageUrl),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(42.dp)
                        .border(1.5.dp, borderColor, CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                        .clip(CircleShape)
                        .align(Alignment.Top),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            } else {
                Spacer(modifier = Modifier.width(74.dp))
            }

            // TODO : APPEAR ON BUBBLE CLICK
            AuthorAndTextMessage(
                msg = msg,
                isUserMe = isUserMe,
                isFirstMessageByAuthor = isFirstMessageByAuthor,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
            )
        }
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(value = false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessVeryLow
                )
            ),
        horizontalAlignment = if (isUserMe) Alignment.End else Alignment.Start
    ) {
        if (expanded) {
            AuthorNameTimestamp(msg)
        }

        val backgroundBubbleColor = if (isUserMe) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

        Column(horizontalAlignment = if (isUserMe) Alignment.End else Alignment.Start,
            modifier = Modifier.clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = !expanded }) {
            Surface(
                color = backgroundBubbleColor,
                shape = if (isUserMe) MineChatBubbleShape else OtherUserChatBubbleShape
            ) {
                Text(
                    text = msg.message!!,
                    style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
                    modifier = Modifier.padding(16.dp),
                )
            }
        }

        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

private val OtherUserChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val MineChatBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    val time = getDate(msg.date!!)
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {

        Text(
            text = msg.sender!!,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}