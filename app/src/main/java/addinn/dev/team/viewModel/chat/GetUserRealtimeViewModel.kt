package addinn.dev.team.viewModel.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.usecase.chat.GetUserRealtimeUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUserRealtimeViewModel @Inject constructor(
    private val getUserRealtimeUseCase: GetUserRealtimeUseCase,
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dataState = MutableStateFlow<Response<User>>(Response.Loading)
    val userInfo: StateFlow<Response<User>> = _dataState

    internal fun getData(username: String) = viewModelScope.launch {
        _loadingState.value = true

        getUserRealtimeUseCase.invoke(username).collectLatest {
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