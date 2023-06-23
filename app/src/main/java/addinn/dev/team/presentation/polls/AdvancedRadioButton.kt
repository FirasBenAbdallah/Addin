package addinn.dev.team.presentation.polls

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdvancedRadioButton(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Local variables
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateDpAsState(if (isSelected) 1.1.dp else 1.dp)
    val color by animateColorAsState(
        if (isSelected) colorScheme.primary else colorScheme.surface
    )

    // Radio button row
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onSelected() }
            )
    ) {
        // Radio button
        Surface(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .scale(scale.value)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onSelected() }
                ),
            color = color
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Radio button text
        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 1f),
            modifier = Modifier.alpha(if (isSelected) 1f else 0.6f)
        )
    }
}