package addinn.dev.team.presentation.register

import addinn.dev.team.utils.navigation.NavigationProvider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun RegisterView(navigator: NavigationProvider) {
    TextButton(onClick = { navigator.navigateBack() }) {
        Text(text = "Navigate back")
    }
}