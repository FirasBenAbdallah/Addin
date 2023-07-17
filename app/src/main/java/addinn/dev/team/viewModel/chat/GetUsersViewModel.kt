package addinn.dev.team.viewModel.chat

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.usecase.chat.GetUsersByDepUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetUsersViewModel @Inject constructor(
    private val getUsersByDepUseCase: GetUsersByDepUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _usersState = MutableStateFlow<Response<List<User>>>(Response.Loading)
    val usersState: StateFlow<Response<List<User>>> = _usersState

    internal fun getUsersByDep(uid: String,dep: String) = viewModelScope.launch {
        _loadingState.value = true

        getUsersByDepUseCase.invoke(uid,dep).collectLatest {
            when (it) {
                is Response.Error -> {
                    _usersState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _usersState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }
}