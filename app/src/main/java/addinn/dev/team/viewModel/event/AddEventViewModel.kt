package addinn.dev.team.viewModel.event

import addinn.dev.domain.entity.event.EventRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.event.AddEventUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val addEvent: AddEventUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dataState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val create: StateFlow<Response<Boolean>> = _dataState

    internal fun addEvent(eventRequest: EventRequest) = viewModelScope.launch {
        _loadingState.value = true

        addEvent.invoke(eventRequest = eventRequest).collectLatest {
            when (it) {
                is Response.Error -> {
                    _dataState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _dataState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }
}