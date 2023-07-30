package addinn.dev.team.viewModel.poll

import addinn.dev.domain.entity.poll.PollRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.poll.UpdatePollUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatePollViewModel @Inject constructor(
    private val updatePollUseCase: UpdatePollUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _pollState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val pollState: StateFlow<Response<Boolean>> = _pollState

    internal fun updatePoll(id: String, pollRequest: PollRequest) = viewModelScope.launch {
        _loadingState.value = true

        updatePollUseCase.invoke(id, pollRequest).collectLatest {
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