package addinn.dev.team.viewModel

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.poll.PollResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.poll.PollUseCase
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
    private val pollUseCase: PollUseCase,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState


    private val _pollState = MutableStateFlow<Response<PollResponse>>(Response.Loading)
    val pollState: StateFlow<Response<PollResponse>> = _pollState

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
}