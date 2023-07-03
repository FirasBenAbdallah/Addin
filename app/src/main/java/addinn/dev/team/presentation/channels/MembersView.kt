package addinn.dev.team.presentation.channels

import addinn.dev.team.R
import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MembersView() {
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
                items(10) {
                    MemberItem()
                }
            }
        }
    }
}

@Composable
fun MemberItem() {

    val snooze = SwipeAction(
        icon = { Text("Snooze") },
        background = Color.Yellow,
        isUndo = true,
        onSwipe = { },
    )


    SwipeableActionsBox(
        endActions = listOf(snooze)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp)
                .animateContentSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.background(color = Color.LightGray, shape = CircleShape)) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "avatar"
                )
            }
            Text(text = "Member Name")
        }
    }
}

/*@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MembersPreview() {
    MembersView()
}*/