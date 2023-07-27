package addinn.dev.team.viewModel.event

import addinn.dev.domain.entity.event.Event
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.event.GetEventsUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetEventsViewModel @Inject constructor(
    private val getEvents: GetEventsUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dataState = MutableStateFlow<Response<List<Event>>>(Response.Loading)
    val eventsList: StateFlow<Response<List<Event>>> = _dataState

    init {
        getGroups()
    }

    private fun getGroups() = viewModelScope.launch {
        _loadingState.value = true

        getEvents.invoke().collectLatest {
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