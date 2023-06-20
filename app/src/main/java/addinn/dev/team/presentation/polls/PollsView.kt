package addinn.dev.team.presentation.polls

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollsView(/*navigator: NavigationProvider?,*/ modifier: Modifier = Modifier) {

    // Local variables
    val focusManager = LocalFocusManager.current
    val polls = remember { mutableStateListOf<PollData>() }
    val votes = remember { mutableStateListOf<PollData>() }
    val showPollDetails = remember { mutableStateOf(false) }
    val showPoll = remember { mutableStateOf(false) }
    val showCount = remember { mutableStateOf(false) }
    val choiceCountInput = remember { mutableStateOf("") }

    // Composable functions
    Scaffold(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // Button to create a new poll
            Button(
                onClick = {
                    showCount.value = true
                    showPollDetails.value = false
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Create Poll",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Polls Number of Choices
            if (showCount.value) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(end = 16.dp)
                ) {
                    OutlinedTextField(
                        value = choiceCountInput.value,
                        onValueChange = { value ->
                            choiceCountInput.value = value
                        },
                        label = { Text("Number of Choices") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            showPoll.value = true
                            val choiceCount = choiceCountInput.value.toIntOrNull() ?: 0
                            val choices = MutableList(choiceCount) { mutableStateOf("") }
                            polls.clear()
                            polls.add(
                                PollData(
                                    mutableStateOf(""),
                                    choices
                                )
                            )
                            focusManager.clearFocus()
                        }),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    /*Button(onClick = {

                    }) {
                        Text(text = "Ok")
                    }*/
                }
            }

            // Polls list body
            polls.forEachIndexed { index, poll ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        if (showPoll.value) {
                            // Display the poll title
                            Text(text = "Poll ${index + 1}", fontWeight = FontWeight.Bold)

                            // Display the poll question
                            OutlinedTextField(
                                value = poll.question.value,
                                onValueChange = { value -> poll.question.value = value },
                                label = { Text("Question") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(onNext = {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }),
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
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(onNext = {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    }),
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                            // View Polls button
                            Button(
                                onClick = {
                                    showPollDetails.value = true
                                    showCount.value = false
                                    showPoll.value = false
                                    votes.add(poll)
                                },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text(text = "View Polls")
                            }
                        }
                    }
                }
            }
            // PollsDetailsView
            votes.forEachIndexed { _, poll ->
                if (showPollDetails.value) {
                    PollsDetailsView(poll = poll)
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPreview() {
    PollsDetailsView(null)
}
*/
