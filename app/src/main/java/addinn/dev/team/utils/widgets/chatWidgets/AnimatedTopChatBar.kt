package addinn.dev.team.utils.widgets.chatWidgets

import addinn.dev.team.R
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedTopChatBar() {
    var expanded by rememberSaveable { mutableStateOf(value = false) }

    Box(
        modifier = Modifier
            .background(
                Color(0xFF0D5881),
                shape = RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = 0.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                )
            )
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            )
            .clickable { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF0D5881), shape = CircleShape)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "avatar",
                )
            }
            Text(
                text = "Usename",
                style = TextStyle(fontSize = 18.sp, color = Color.White),
                modifier = Modifier.padding(top = 6.dp)
            )
            Text(
                text = "online",
                style = TextStyle(fontSize = 14.sp, color = Color.White),
                modifier = Modifier.padding(top = 4.dp)
            )
            if (expanded) {
                Box(modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier.background(
                                color = Color.LightGray.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(14.dp)
                            )
                        ) {
                            IconButton(
                                onClick = {},
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_search_24),
                                    contentDescription = "search Icon",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(40.dp),
                                    tint = Color.White
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.background(
                                color = Color.LightGray.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(14.dp)
                            )
                        ) {
                            IconButton(
                                onClick = {},
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_image_24),
                                    contentDescription = "image Icon",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(40.dp), tint = Color.White
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.background(
                                color = Color.LightGray.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(14.dp)
                            )
                        ) {
                            IconButton(
                                onClick = {},
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_settings_24),
                                    contentDescription = "settings Icon",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(40.dp), tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AnimatedPreview() {
    AnimatedTopChatBar()
}