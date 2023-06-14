package addinn.dev.team.presentation.polls

import addinn.dev.team.utils.navigation.NavigationProvider
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
//@Destination(start = true)
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
                }, modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Create Poll")
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
                    // Button
                    /*Button(
                        onClick = { }, modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(text = "Submit Vote")
                    }*/
                }
            }
        }

    }
}

data class Poll(
    val question: MutableState<String>,
    val choices: MutableList<MutableState<String>>,
    var selectedOption: String = ""
)

@Composable
fun PollsDetailsView(poll: Poll) {
    val isTextVisible = remember { mutableStateOf(false) }
    val composedChoicesText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)

            .fillMaxWidth()
            .background(Color.LightGray, shape = MaterialTheme.shapes.large)
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
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = poll.selectedOption == choice.value,
                    onClick = {
                        poll.selectedOption = choice.value
                        isTextVisible.value = true
                    },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = choice.value)
            }
        }

        // Button
        Button(
            onClick = {
                composedChoicesText.value = poll.choices.joinToString("\n") { it.value }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Submit Vote")
        }

        if (isTextVisible.value) {
            Text(
                text = "Selected Choices:\n${composedChoicesText.value}",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


//@Destination(start = true)
/*@Composable
fun PollsView(navigator: NavigationProvider?) {
    val polls = remember { mutableStateListOf<Poll>() }
    val showPollDetails = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        Button(
            onClick = {
                polls.add(Poll(mutableStateOf(""), mutableListOf(mutableStateOf(""), mutableStateOf(""), mutableStateOf(""))))
                showPollDetails.value = false
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Create Poll")
        }

        polls.forEachIndexed { index, poll ->
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
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
                        onValueChange = { value -> poll.choices[choiceIndex].value = value },
                        label = { Text("Choice ${choiceIndex + 1}") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = { showPollDetails.value = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    println(showPollDetails.value)
                    Text(text = "View Polls")
                }

                if (showPollDetails.value) {
                    PollsDetailsView(poll)
//                    showPollDetails.value = false
                    println(showPollDetails.value)
                }
            }
        }
    }
}

data class Poll(
    val question: MutableState<String>,
    val choices: MutableList<MutableState<String>>,
    var selectedOption: String = ""
)

private fun viewPolls(poll: Poll) {
    // Handle the action to view polls
    println("Viewing Polls - Question: ${poll.question.value}, Choices: ${poll.choices.map { it.value }}")
}

@Composable
fun PollsDetailsView(poll: Poll) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = poll.selectedOption.equals(choice.toString()) ,
                    onClick = { poll.selectedOption = choice.toString() },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = choice.value)
            }
        }

        // Button
        Button(
            onClick = { *//* Handle button click *//* },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Submit Vote")
        }
    }
}*/


/*fun PollsView(navigator: NavigationProvider?) {
    val selectedChoice = remember { mutableStateOf("") }
    val showLabel = selectedChoice.value.isNotBlank()
    val red = remember { mutableStateOf(0) }
    val blue = remember { mutableStateOf(0) }
    val green = remember { mutableStateOf(0) }

    val pollQuestion = remember { mutableStateOf("") }
    val pollChoices = remember { mutableStateOf(listOf("", "", "")) }

    Scaffold {
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "What is your favorite color?",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                ChoiceItem(text = "Red", selectedChoice.value == "Red") {
                    selectedChoice.value = "Red"
                }
                ChoiceItem(text = "Blue", selectedChoice.value == "Blue") {
                    selectedChoice.value = "Blue"
                }
                ChoiceItem(text = "Green", selectedChoice.value == "Green") {
                    selectedChoice.value = "Green"
                }

                Button(
                    onClick = {
                        when (selectedChoice.value) {
                            "Red" -> red.value++
                            "Blue" -> blue.value++
                            "Green" -> green.value++
                            else -> {
                                // Handle the action for the other choices
                                println("Action for the other choices")
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "View Polls")
                }
                val pollsResult = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Polls result:\n")
                    }
                    append("Red: ${red.value}\n")
                    append("Blue: ${blue.value}\n")
                    append("Green: ${green.value}\n")
                }

                if (red.value > 0 || blue.value > 0 || green.value > 0) {
                    Text(
                        text = pollsResult,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        val question = pollQuestion.value
                        val choices = pollChoices.value.filter { it.isNotBlank() }
                        // Create the new poll using the question and choices
                        // You can implement the desired logic here
                        println("New Poll: Question - $question, Choices - $choices")
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Create Poll")
                }

                TextField(
                    value = pollQuestion.value,
                    onValueChange = { pollQuestion.value = it },
                    label = { Text("Question") },
                    modifier = Modifier.padding(top = 16.dp)
                )

                for (i in pollChoices.value.indices) {
                    TextField(
                        value = pollChoices.value[i],
                        onValueChange = { newValue ->
                            val updatedChoices = pollChoices.value.toMutableList()
                            updatedChoices[i] = newValue
                            pollChoices.value = updatedChoices
                        },
                        label = { Text("Choice ${i + 1}") },
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

            }
        }
    }
}*/

/*@Composable
fun ChoiceItem(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}*/


/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResetPreview() {
    PollsView(null)
}
*/
