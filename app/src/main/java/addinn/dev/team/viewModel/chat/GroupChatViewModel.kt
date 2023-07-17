package addinn.dev.team.viewModel.chat

import addinn.dev.data.model.PushNotification
import addinn.dev.data.remote.MessageNotificationApi
import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.chat.GetGroupMessageUseCase
import addinn.dev.domain.usecase.chat.SendGroupMessageUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val sendGroupMessageUseCase: SendGroupMessageUseCase,
    private val getGroupMessageUseCase: GetGroupMessageUseCase,
    private val api: MessageNotificationApi
) : ViewModel() {
    // SEND MESSAGE
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _messageState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val messageState: StateFlow<Response<Boolean>> = _messageState

    internal fun sendMessage(groupName: String, messageRequest: MessageRequest) = viewModelScope.launch {
        _loadingState.value = true

        sendGroupMessageUseCase.invoke(groupName,messageRequest).collectLatest {
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

    // GET MESSAGES
    private val _loadingMsgState = MutableStateFlow(false)
    val loadingMsgState: StateFlow<Boolean> = _loadingMsgState

    private val _allMessagesState = MutableStateFlow<Response<List<Message>>>(Response.Loading)
    val allMessagesState: StateFlow<Response<List<Message>>> = _allMessagesState

    internal fun getAllMessages(groupName:String) = viewModelScope.launch {
        _loadingMsgState.value = true

        getGroupMessageUseCase.invoke(groupName).collectLatest {
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

    // SEND NOTIFICATION
    fun postNotification(notification: PushNotification) {
        viewModelScope.launch {
            try {
                val response = api.postNotification(notification)
                if (response.isSuccessful) {
                } else {
                    val errorMessage = response.errorBody()!!.charStream().readText()
                }
            } catch (e: Exception) {
            }
        }
    }
}