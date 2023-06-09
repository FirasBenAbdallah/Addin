package addinn.dev.team.utils.widgets

import addinn.dev.team.R
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun PasswordToggleIcon(
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    val visibilityIcon: ImageVector = if (isVisible) {
        ImageVector.vectorResource(id = R.drawable.outline_visibility_24)
    } else {
        ImageVector.vectorResource(id = R.drawable.outline_visibility_off_24)
    }

    IconButton(
        onClick = onToggle
    ) {
        Icon(
            imageVector = visibilityIcon,
            contentDescription = if (isVisible) "Hide password" else "Show password"
        )
    }
}