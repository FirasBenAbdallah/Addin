package addinn.dev.team.viewModel

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.poll.GetPollUseCase
import addinn.dev.domain.usecase.poll.PollUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PollViewModel @Inject constructor(
    private val pollUseCase: PollUseCase,
    private val getPollUseCase: GetPollUseCase,
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _pollState = MutableStateFlow<Response<PollResponse>>(Response.Loading)
    private val _pollStates = MutableStateFlow<Response<List<Poll>>>(Response.Loading)
    val pollState: StateFlow<Response<List<Poll>>> = _pollStates

    init {
        startPollExpirationChecker()
    }

    private fun startPollExpirationChecker() {
        viewModelScope.launch {
            while (true) {
                checkAndDeleteExpiredPolls()
                delay(60000) // Check every 1 minute
            }
        }
    }


    private fun checkAndDeleteExpiredPolls() {
        val firestore = FirebaseFirestore.getInstance()
        val currentTime = System.currentTimeMillis()

        firestore.collection("polls")
            .whereLessThan("creationTimestamp", currentTime - 60000) // Check for polls older than 1 minute
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.delete()
                        .addOnSuccessListener {
                            // Poll deleted successfully
                        }
                        .addOnFailureListener { exception ->
                            // Handle the failure while deleting the poll
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure while fetching expired polls
            }
    }
    internal fun poll(pollRequest: PollRequest) = viewModelScope.launch {
        _loadingState.value = true

        pollUseCase.invoke(pollRequest).collectLatest {
            when (it) {
                is Response.Error -> {
                    _pollState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _pollState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }

    internal fun getPolls() = viewModelScope.launch {
        _loadingState.value = true
        getPollUseCase.invoke().collectLatest { response ->
            when (response) {
                is Response.Error -> {
                    _pollStates.value = Response.Error(response.error)
                    _loadingState.value = false
                }
                Response.Loading -> {
                }
                is Response.Success -> {
                    val polls = response.data // Extract the list of polls from the response
                    _pollStates.value = Response.Success(polls)
                    _loadingState.value = false
                }
            }
        }
    }
}