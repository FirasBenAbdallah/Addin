package addinn.dev.team.presentation.polls

import addinn.dev.team.utils.navigation.NavigationProvider
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollsView(navigator: NavigationProvider?, modifier: Modifier = Modifier) {
    val polls = remember { mutableStateListOf<Poll>() }
    val votes = remember { mutableStateListOf<Poll>() }
    val showPollDetails = remember { mutableStateOf(false) }


    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Button(
                onClick = {
                    polls.clear()
                    polls.add(
                        Poll(
                            mutableStateOf(""),
                            mutableListOf(
                                mutableStateOf(""),
                                mutableStateOf(""),
                                mutableStateOf("")
                            )
                        )
                    )

                    showPollDetails.value = false
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(50))
            ) {
//                Text(text = "Create Poll")
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Create Poll",
                    modifier = Modifier.size(24.dp)
                )
            }

            polls.forEachIndexed { index, poll ->
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    if (!showPollDetails.value) {
                        Text(text = "Poll ${index + 1}", fontWeight = FontWeight.Bold)

                        // Display the poll question
                        OutlinedTextField(
                            value = poll.question.value,
                            onValueChange = { value -> poll.question.value = value },
                            label = { Text("Question") },
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Display the poll choices
                        poll.choices.forEachIndexed { choiceIndex, choice ->
                            OutlinedTextField(
                                value = choice.value,
                                onValueChange = { value ->
                                    poll.choices[choiceIndex].value = value
                                },
                                label = { Text("Choice ${choiceIndex + 1}") },
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        Button(
                            onClick = { showPollDetails.value = true; votes.add(poll) },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text(text = "View Polls")
                        }
                    }
                }
            }
            votes.forEachIndexed { index, poll ->
                if (showPollDetails.value) {
                    PollsDetailsView(poll = poll)
                }
            }
        }

    }
}


@Composable
fun PollsDetailsView(poll: Poll,modifier: Modifier = Modifier) {
    val isTextVisible = remember { mutableStateOf(false) }
    val composedChoicesText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.LightGray, shape = shapes.extraLarge)
            .padding(16.dp)
    ) {
        // Poll question label
        Text(
            text = "Question: ${poll.question.value}",
            fontWeight = FontWeight.Bold,
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

        Text(
            text = "Total Votes: ${poll.totalSelectionCount.value}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 8.dp),
            color = colorScheme.onBackground
        )


    }
}

@Composable
fun AdvancedRadioButton(
    text: String,
    isSelected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateDpAsState(if (isSelected) 1.1.dp else 1.dp)
    val color by animateColorAsState(
        if (isSelected) colorScheme.primary else colorScheme.surface
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onSelected() }
            )
    ) {
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

        Text(
            text = text,
            fontSize = 16.sp,
            color = if (isSelected) colorScheme.primary else colorScheme.onSurface.copy(alpha = 1f),
            modifier = Modifier.alpha(if (isSelected) 1f else 0.6f)
        )
    }
}

data class Poll(
    val question: MutableState<String>,
    val choices: MutableList<MutableState<String>>,
    var selectedOptionIndex: MutableState<Int?> = mutableStateOf(null),
    var selectionCounts: MutableList<MutableState<Int>> = MutableList(choices.size) {
        mutableStateOf(
            0
        )
    },
    var totalSelectionCount: MutableState<Int> = mutableStateOf(0)
)

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPreview() {
    PollsDetailsView(null)
}
*/
