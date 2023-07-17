package addinn.dev.team.viewModel.chat

import addinn.dev.domain.entity.chat.Chat
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.chat.GetLatestMessagesUseCase
import addinn.dev.domain.usecase.chat.SetMessageSeenUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val useCase: GetLatestMessagesUseCase,
    private val setMessageSeenUseCase: SetMessageSeenUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _requestState = MutableStateFlow<Response<List<Chat>>>(Response.Loading)
    val requestState: StateFlow<Response<List<Chat>>> = _requestState

    internal fun getLastestMessages(uid: String) = viewModelScope.launch {
        _loadingState.value = true

        useCase.invoke(uid).collectLatest {
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

    internal fun setMessageSeen(chatId: String) = viewModelScope.launch {
        setMessageSeenUseCase.invoke(chatId).collectLatest {
            when (it) {
                is Response.Error -> {
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                }
            }
        }
    }
}