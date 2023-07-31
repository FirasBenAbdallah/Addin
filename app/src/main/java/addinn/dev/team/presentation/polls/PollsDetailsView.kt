package addinn.dev.team.presentation.polls

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.team.utils.widgets.pollWidget.AdvancedRadioButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PollsDetailsView(poll: Poll,onSelect : (Int) -> Unit,userId:String) {

    val choices = remember { mutableStateOf(emptyList<String>()) }
    val votes = remember { mutableStateOf(emptyList<Int>()) }
    val selectedIndex: MutableState<Int?> = remember { mutableStateOf(null) }
    val percentage = remember { mutableStateOf(0.0) }

    choices.value = listOf(
        poll.choice1!!,
        poll.choice2!!,
        poll.choice3!!
    )

    votes.value = listOf(
        poll.choiceVote1!!,
        poll.choiceVote2!!,
        poll.choiceVote3!!
    )

    val isClickable = if(poll.votesBy == null) true else !poll.votesBy?.contains(userId)!!

    val totalVotes = if(poll.totalVotes == null) 0 else poll.totalVotes

    // Poll details column
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = shapes.extraLarge)
            .padding(16.dp)
    ) {
        // Poll question label
        Text(
            text = poll.question!!,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if(!poll.votesBy.isNullOrEmpty() && poll.votesBy?.contains(userId)!!){
            Text(
                text = "You have already voted",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Poll choices radio buttons
        choices.value.forEachIndexed { index, choice ->
            AdvancedRadioButton(
                text = choice,
                isSelected = selectedIndex.value == index,
                onSelected = {
                    selectedIndex.value = index
                    onSelect(index)
                },
                isClickable = isClickable,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Poll choices votes
        votes.value.forEachIndexed { index, vote ->
            val calculatedPercentage = (vote.toDouble() / totalVotes!!.toDouble()) * 100.0
            percentage.value = calculatedPercentage

            val formattedPercentage = if (percentage.value % 1 == 0.0) {
                "%.0f%%".format(percentage.value)
            } else {
                "%.2f%%".format(percentage.value)
            }

            Text(
                text = "${choices.value[index]}: $formattedPercentage",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Poll total votes label
        Text(
            text = "Total votes: $totalVotes",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}