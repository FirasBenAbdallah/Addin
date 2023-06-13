package addinn.dev.team.presentation.events

import addinn.dev.team.presentation.recoverPass.ResetView
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
fun EventsView(navigator: NavigationProvider?) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Text("Events")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EventsPreview() {
    EventsView(null)
}