package addinn.dev.team.viewModel.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.usecase.auth.SearchUsersUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _requestState = MutableStateFlow<Response<List<User>>>(Response.Loading)
    val requestState: StateFlow<Response<List<User>>> = _requestState

    internal fun search(uid: String,search: String) = viewModelScope.launch {
        _loadingState.value = true

        searchUsersUseCase.invoke(uid,search).collectLatest {
            when (it) {
                is Response.Error -> {
                    _requestState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _requestState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }
}