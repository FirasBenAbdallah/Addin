package addinn.dev.team.presentation.login

import addinn.dev.team.utils.navigation.NavigationProvider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Destination(start = true)
@Composable
fun LoginView(navigator: NavigationProvider) {
    TextButton(onClick = { navigator.navigateToRegister() }) {
        Text(text = "Navigate to register")
    }
}