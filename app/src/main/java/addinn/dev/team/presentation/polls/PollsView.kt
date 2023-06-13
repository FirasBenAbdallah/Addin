package addinn.dev.team.presentation.polls

import addinn.dev.team.presentation.events.EventsView
import addinn.dev.team.utils.navigation.NavigationProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PollsView(navigator: NavigationProvider?) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text("Polls")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPreview() {
    PollsView(null)
}