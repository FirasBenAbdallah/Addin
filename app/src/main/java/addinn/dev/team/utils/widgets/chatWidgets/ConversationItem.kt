package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
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
fun ConversationItem(navigator: NavigationProvider) {
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
                modifier = Modifier.size(80.dp), shape = CircleShape
            ) {
                Box(modifier = Modifier.background(color = Color.LightGray.copy(0.5f), shape = CircleShape)) {
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
                        text = "username",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "recent message ",
                        style = TextStyle(
                            fontWeight = FontWeight.Light
                        )
                    )
                }
                Column {
                    Text(
                        text = "12:00",
                        fontWeight = FontWeight.ExtraLight,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.outline_mark_chat_unread_24),
                        tint = Color(0xFF0D5881),
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

