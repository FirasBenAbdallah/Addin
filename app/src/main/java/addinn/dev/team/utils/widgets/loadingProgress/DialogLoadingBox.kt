package addinn.dev.team.utils.widgets.loadingProgress

import addinn.dev.team.utils.theme.Addinn_blue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogBoxLoading(
    cornerRadius: Dp = 16.dp,
    paddingStart: Dp = 56.dp,
    paddingEnd: Dp = 56.dp,
    paddingTop: Dp = 32.dp,
    paddingBottom: Dp = 32.dp,
    progressIndicatorSize: Dp = 80.dp
) {

    Dialog(
        onDismissRequest = {
        }
    ) {
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = paddingStart, end = paddingEnd, top = paddingTop),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                ProgressIndicatorLoading(
                    progressIndicatorSize = progressIndicatorSize,
                    progressIndicatorColor = Addinn_blue
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier
                        .padding(bottom = paddingBottom),
                    text = "Loading...",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    )
                )
            }
        }
    }
}