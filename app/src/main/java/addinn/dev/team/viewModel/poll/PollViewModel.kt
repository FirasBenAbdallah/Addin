package addinn.dev.team.viewModel.poll

import addinn.dev.domain.entity.poll.Poll
import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.poll.AddPollUseCase
import addinn.dev.domain.usecase.poll.GetPollUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PollViewModel @Inject constructor(
    private val addpollUseCase: AddPollUseCase,
    private val getPollUseCase: GetPollUseCase,
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    init {
        getPolls()
    }

    private val _pollState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val pollState: StateFlow<Response<Boolean>> = _pollState

    internal fun addPoll(pollRequest: PollRequest) = viewModelScope.launch {
        _loadingState.value = true

        addpollUseCase.invoke(pollRequest).collectLatest {
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

    private val _pollsState = MutableStateFlow<Response<List<Poll>>>(Response.Loading)
    val pollsState: StateFlow<Response<List<Poll>>> = _pollsState

    private fun getPolls() = viewModelScope.launch {
        _loadingState.value = true
        getPollUseCase.invoke().collectLatest { response ->
            when (response) {
                is Response.Error -> {
                    _pollsState.value = Response.Error(response.error)
                    _loadingState.value = false
                }

                Response.Loading -> {
                }

                is Response.Success -> {
                    val polls = response.data // Extract the list of polls from the response
                    _pollsState.value = Response.Success(polls)
                    _loadingState.value = false
                }
            }
        }
    }
}