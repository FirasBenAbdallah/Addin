package addinn.dev.team.presentation.polls

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.team.utils.navigation.NavigationProvider
import addinn.dev.team.utils.widgets.loadingProgress.DialogBoxLoading
import addinn.dev.team.viewModel.SharedViewModel
import addinn.dev.team.viewModel.poll.PollViewModel
import addinn.dev.team.viewModel.poll.UpdatePollViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PollsView(
    modifier: Modifier = Modifier,
    navigator: NavigationProvider,
    viewModel: PollViewModel = hiltViewModel(),
    updateViewModel: UpdatePollViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val polls = remember { mutableStateOf(emptyList<Poll>()) }
    val currentUser = sharedViewModel.getUser()
    val pollsState = viewModel.pollsState.collectAsState()
    val loadingState = viewModel.loadingState.collectAsState()

    if (loadingState.value) {
        DialogBoxLoading()
    }

    when (pollsState.value) {
        is Response.Error -> {
        }

        is Response.Success -> {
            polls.value = (pollsState.value as Response.Success<List<Poll>>).data
        }

        Response.Loading -> {
        }
    }

    Scaffold(modifier = modifier, floatingActionButton = {
        if (currentUser.department == "Administration") {
            FloatingActionButton(onClick = { navigator.navigateToAddPoll() }) {
                Icon(imageVector = Icons.Outlined.AddCircle, contentDescription = "add poll")
            }
        }
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(polls.value) { poll ->

                PollsDetailsView(
                    poll = poll,
                    userId = currentUser.id!!,
                    onSelect = { index->
                        val newTotalVotes = poll.totalVotes!! + 1
                        val newVotesBy = if(poll.votesBy == null) {
                            listOf(currentUser.id!!)
                        } else if(poll.votesBy!!.contains(currentUser.id!!)){
                            poll.votesBy!!
                        } else {
                            poll.votesBy!!.plus(currentUser.id!!)
                        }
                        val newChoiceVote1 = if (index == 0) {
                            poll.choiceVote1!! + 1
                        } else {
                            poll.choiceVote1
                        }

                        val newChoiceVote2 = if (index == 1) {
                            poll.choiceVote2!! + 1
                        } else {
                            poll.choiceVote2
                        }

                        val newChoiceVote3 = if (index == 2) {
                            poll.choiceVote3!! + 1
                        } else {
                            poll.choiceVote3
                        }

                        val pollRequest = PollRequest(
                            question = poll.question!!,
                            choice1 = poll.choice1!!,
                            choice2 = poll.choice2!!,
                            choice3 = poll.choice3!!,
                            choiceVote1 = newChoiceVote1!!,
                            choiceVote2 = newChoiceVote2!!,
                            choiceVote3 = newChoiceVote3!!,
                            totalVotes = newTotalVotes,
                            votesBy = newVotesBy
                        )
                        updateViewModel.updatePoll(poll.id!!,pollRequest)
                    }
                )
            }
        }
    }
}