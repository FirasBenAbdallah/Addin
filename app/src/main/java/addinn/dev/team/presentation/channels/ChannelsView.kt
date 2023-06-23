package addinn.dev.team.presentation.channels

import addinn.dev.team.R
import addinn.dev.team.utils.navigation.NavigationProvider
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsView(navigator: NavigationProvider, modifier: Modifier = Modifier) {

    val data = listOf(
        ChannelItem(
            name = "Main Channel",
            image = R.drawable.conversation_ill,
            colors = listOf(
                Color(0xffBE93C5),
                Color(0xff7BC6CC),
            ),
            onClick = {}),
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

    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mobile dev Channel", style = TextStyle(
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
                    painter = painterResource(id = R.drawable.mobile_pic),
                    contentDescription = "Mobile dev Channel",
                )
            }

            Text(
                text = "12 members", style = TextStyle(
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