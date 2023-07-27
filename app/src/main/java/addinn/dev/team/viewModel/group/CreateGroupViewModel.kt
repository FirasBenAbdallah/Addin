package addinn.dev.team.viewModel.group

import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.group.GroupRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.group.CreateGroupUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
    private val createGroupUseCase: CreateGroupUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _dataState = MutableStateFlow<Response<String>>(Response.Loading)
    val create: StateFlow<Response<String>> = _dataState

    internal fun createGroup(groupRequest: GroupRequest,messageRequest: MessageRequest) = viewModelScope.launch {
        _loadingState.value = true

        createGroupUseCase.invoke(groupRequest = groupRequest, messageRequest = messageRequest).collectLatest {
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