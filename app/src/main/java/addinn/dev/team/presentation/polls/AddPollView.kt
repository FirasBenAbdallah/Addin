package addinn.dev.team.presentation.polls

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.poll.PollViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddPollView(
    navigator: NavigationProvider,
    viewModel: PollViewModel = hiltViewModel()
) {

    val questionField = remember { mutableStateOf("") }
    val choice1 = remember { mutableStateOf("") }
    val choice2 = remember { mutableStateOf("") }
    val choice3 = remember { mutableStateOf("") }

    val loadingState = viewModel.loadingState.collectAsState()
    val addState = viewModel.pollState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (loadingState.value) {
        DialogBoxLoading()
    }

    LaunchedEffect(addState.value) {
        when (addState.value) {
            is Response.Error -> {
                val error = (addState.value as Response.Error).error
                snackbarHostState.showSnackbar(error)
            }

            Response.Loading -> {

            }

            is Response.Success -> {
                navigator.navigateBack()
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Add Poll") }, navigationIcon = {
            IconButton(onClick = { navigator.navigateBack() }) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
            }
        })
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = questionField.value,
                onValueChange = {
                    questionField.value = it
                },
                label = { Text("Question") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            OutlinedTextField(
                value = choice1.value,
                onValueChange = {
                    choice1.value = it
                },
                label = { Text("First choice") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            OutlinedTextField(
                value = choice2.value,
                onValueChange = {
                    choice2.value = it
                },
                label = { Text("Second choice") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            OutlinedTextField(
                value = choice3.value,
                onValueChange = {
                    choice3.value = it
                },
                label = { Text("Third choice") },
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                textStyle = TextStyle(fontSize = 16.sp),
            )

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = {
                    val pollRequest = PollRequest(
                        question = questionField.value,
                        choice1 = choice1.value,
                        choice2 = choice2.value,
                        choice3 = choice3.value,
                        choiceVote1 = 0,
                        choiceVote2 = 0,
                        choiceVote3 = 0,
                        totalVotes = 0
                    )
                    viewModel.addPoll(pollRequest)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(text = "Add Poll")
            }
        }

    }
}