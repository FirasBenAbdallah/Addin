package addinn.dev.team.viewModel.group

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.group.GetGrpMessageUseCase
import addinn.dev.domain.usecase.group.SendGrpMessageUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupMessagesViewModel @Inject constructor(
    private val getMessages: GetGrpMessageUseCase,
    private val sendUseCase: SendGrpMessageUseCase,
) : ViewModel() {

    // GET MESSAGES
    private val _loadingMsgState = MutableStateFlow(false)
    val loadingMsgState: StateFlow<Boolean> = _loadingMsgState

    private val _allMessagesState = MutableStateFlow<Response<List<Message>>>(Response.Loading)
    val allMessagesState: StateFlow<Response<List<Message>>> = _allMessagesState

    internal fun getAllMessages(chatId: String) = viewModelScope.launch {
        _loadingMsgState.value = true

        getMessages.invoke(chatId).collectLatest {
            when (it) {
                is Response.Error -> {
                    _allMessagesState.value = Response.Error(it.error)
                    _loadingMsgState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _allMessagesState.value = Response.Success(it.data)
                    _loadingMsgState.value = false
                }
            }
        }
    }

    // SEND MESSAGE
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _messageState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val messageState: StateFlow<Response<Boolean>> = _messageState

    internal fun sendMessage(groupId: String, messageRequest: MessageRequest) =
        viewModelScope.launch {
            _loadingState.value = true

            sendUseCase.invoke(groupId, messageRequest).collectLatest {
                when (it) {
                    is Response.Error -> {
                        _messageState.value = Response.Error(it.error)
                        _loadingState.value = false
                    }

                    Response.Loading -> {

                    }

                    is Response.Success -> {
                        _messageState.value = Response.Success(it.data)
                        _loadingState.value = false
                    }
                }
            }
        }
}