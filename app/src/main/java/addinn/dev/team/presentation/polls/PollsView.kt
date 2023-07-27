package addinn.dev.team.presentation.polls

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.viewModel.PollViewModel
import addinn.dev.team.viewModel.SharedViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType", "ModifierParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollsView(/*navigator: NavigationProvider?,*/viewModel: PollViewModel = hiltViewModel(),
              modifier: Modifier = Modifier,
              sharedViewModel: SharedViewModel = hiltViewModel()
) {

    // Drop down menu
    val listOfChoice = listOf("2", "3", "4", "5", "6", "7", "8", "9", "10")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

    // Local variables
    val focusManager = LocalFocusManager.current
    val polls = remember { mutableStateListOf<PollData>() }
    val votes = remember { mutableStateListOf<PollData>() }
    val showPollDetails = remember { mutableStateOf(false) }
    val showPoll = remember { mutableStateOf(false) }
    val showCount = remember { mutableStateOf(false) }
//    val choiceCountInput = remember { mutableStateOf("") }
    val currentUser = sharedViewModel.getUser()
    val pollState by viewModel.pollState.collectAsState()

    // Trigger the getPolls() function when the composable is first displayed
    val effectLaunched = remember { mutableStateOf(false) }


    // Get the pollState value and based on that show the appropriate composable
    when (pollState) {
        is Response.Error -> {
            // Handle error state
            Text(text = "Error: ${(pollState as Response.Error).error}")
        }

        is Response.Success -> {
            // Handle success state
            val pollsAll = (pollState as Response.Success<List<Poll>>).data

            if (pollsAll.isNotEmpty()) {
                pollsAll.forEachIndexed { i, _ ->
                    votes.add(
                        pollsAll[i].let {
                            PollData(
                                mutableStateOf(it.id!!),
                                mutableStateOf(it.question ?: "Question not available"),
                                mutableListOf(
                                    mutableStateOf(it.choice1 ?: "Choice1 not available"),
                                    mutableStateOf(it.choice2 ?: "Choice2 not available"),
                                    mutableStateOf(it.choice3 ?: "Choice3 not available")
                                ),
                                mutableStateListOf(
                                    mutableStateOf(it.choiceVote1 ?: "0.00%"),
                                    mutableStateOf(it.choiceVote2 ?: "0.00%"),
                                    mutableStateOf(it.choiceVote3 ?: "0.00%")
                                )
                            )
                        }
                    )
                }
            } else {
                // Handle empty state
                Text(text = "No polls found")
            }
        }

        Response.Loading -> {
            // Handle loading state if needed
            Text(text = "Loading...")
        }
    }
    // LaunchedEffect to fetch data only when effectLaunched is false
    LaunchedEffect(key1 = Unit) {
        if (!effectLaunched.value) {
            viewModel.getPolls()
            effectLaunched.value = true
        }
    }

    // Check if the user is an admin or not
    if (currentUser.department == "Administration") {
        // Composable functions
        Scaffold(modifier = modifier) {
            Column(
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp)
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
                    Box(
                        modifier = Modifier
                            .width(250.dp)
                            .shadow(0.dp, shape = RoundedCornerShape(8.dp), clip = true)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it },
                        ) {
                            OutlinedTextField(
                                value = selectedText,
                                onValueChange = {},
                                shape = RoundedCornerShape(10.dp),
                                readOnly = true,
                                label = { Text("Choice number") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = Color.LightGray,
                                    unfocusedBorderColor = Color.LightGray
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                textStyle = TextStyle(fontSize = 16.sp),
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.List,
                                        contentDescription = "Home",
                                        tint = Color.Gray
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                }
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                listOfChoice.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(text = item) },
                                        onClick = {
                                            selectedText = item
                                            expanded = false
                                            showPoll.value = true
                                            val choices =
                                                MutableList(item.toInt()) { mutableStateOf("") }
                                            val choiceVotes =
                                                MutableList(item.toInt()) { mutableStateOf("0.00%") }
                                            polls.clear()
                                            polls.add(
                                                PollData(
                                                    mutableStateOf(""),
                                                    mutableStateOf(""),
                                                    choices,
                                                    choiceVotes
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
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
                                        val pollRequest = PollRequest(
                                            question = poll.question.value,
                                            choice1 = poll.choices[0].value,
                                            choice2 = poll.choices[1].value,
                                            choice3 = poll.choices[2].value,
                                            choiceVote1 = "0.00%",
                                            choiceVote2 = "0.00%",
                                            choiceVote3 = "0.00%"
                                        )

                                        viewModel.poll(pollRequest)
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
//                    if (showPollDetails.value) {
                        PollsDetailsView(poll = poll)
//                    }
                }
            }
        }
    } else {
        Scaffold(modifier = modifier) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                votes.forEachIndexed { j, poll ->
                    PollsDetailsView(poll = poll)
                }
            }
        }
    }
}