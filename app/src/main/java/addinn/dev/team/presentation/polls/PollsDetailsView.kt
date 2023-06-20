package addinn.dev.team.presentation.polls

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PollsDetailsView(poll: PollData, modifier: Modifier = Modifier) {

    // Local variables
    val isTextVisible = remember { mutableStateOf(false) }
    val composedChoicesText = remember { mutableStateOf("") }

    // Poll details column
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = shapes.extraLarge)
            .padding(16.dp)
    ) {
        // Poll question label
        Text(
            text = poll.question.value,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Poll choices radio buttons
        poll.choices.forEachIndexed { index, choice ->
            AdvancedRadioButton(
                text = choice.value,
                isSelected = poll.selectedOptionIndex.value == index,
                onSelected = {
                    poll.selectedOptionIndex.value = index
                    isTextVisible.value = true
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Button Submit Vote
        Button(
            onClick = {
                poll.selectedOptionIndex.value?.let { selectedIndex ->
                    poll.selectionCounts[selectedIndex].value++
                    poll.totalSelectionCount.value++
                }
                composedChoicesText.value = poll.choices.joinToString("\n") { it.value }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Submit Vote")
        }

        // Poll choices text
        if (poll.totalSelectionCount.value > 0) {
            poll.choices.forEachIndexed { index, choice ->
                val stat =
                    poll.selectionCounts[index].value / poll.totalSelectionCount.value.toFloat() * 100
                val statString = String.format("%.2f", stat)
                Text(
                    text = "${choice.value}: ${statString}%",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            Text(
                text = "No votes yet",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Poll choices text
        Text(
            text = "Total Votes: ${poll.totalSelectionCount.value}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp),
            color = colorScheme.onBackground
        )
    }
}