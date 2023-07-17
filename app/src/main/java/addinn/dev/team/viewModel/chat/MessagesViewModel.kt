package addinn.dev.team.viewModel.chat

import addinn.dev.domain.entity.chat.Message
import addinn.dev.domain.entity.chat.MessageRequest
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.chat.GetMessagesUseCase
import addinn.dev.domain.usecase.chat.SendMessageUseCase
import addinn.dev.data.model.PushNotification
import addinn.dev.data.remote.MessageNotificationApi
import addinn.dev.domain.entity.chat.ChatRequest
import addinn.dev.domain.usecase.chat.IsTypingUseCase
import addinn.dev.domain.usecase.chat.ListenIsTypingUseCase
import addinn.dev.domain.usecase.chat.RemoveIsTypingUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getAllMessagesUseCase: GetMessagesUseCase,
    private val isTypingUseCase: IsTypingUseCase,
    private val removeIsTypingUseCase: RemoveIsTypingUseCase,
    private val listenIsTypingUseCase: ListenIsTypingUseCase,
    private val api: MessageNotificationApi
) : ViewModel() {

    // SEND MESSAGE
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _messageState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val messageState: StateFlow<Response<Boolean>> = _messageState

    internal fun sendMessage(chatRequest: ChatRequest,messageRequest: MessageRequest) = viewModelScope.launch {
        _loadingState.value = true

        sendMessageUseCase.invoke(chatRequest,messageRequest).collectLatest {
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

    internal fun getAllMessages(chatId:String) = viewModelScope.launch {
        _loadingMsgState.value = true

        getAllMessagesUseCase.invoke(chatId).collectLatest {
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

    // IS TYPING
    private val _isTypingState = MutableStateFlow<Response<List<String>>>(Response.Loading)
    val isTypingState: StateFlow<Response<List<String>>> = _isTypingState
    internal fun sendIsTyping(chatId:String,isTyping: String) = viewModelScope.launch {

        isTypingUseCase.invoke(chatId,isTyping).collectLatest {
            when (it) {
                is Response.Error -> {
                    /*_allMessagesState.value = Response.Error(it.error)
                    _loadingMsgState.value = false*/
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                   /* _allMessagesState.value = Response.Success(it.data)
                    _loadingMsgState.value = false*/
                }
            }
        }
    }

    internal fun removeIsTyping(chatId:String,isTyping: String) = viewModelScope.launch {

        removeIsTypingUseCase.invoke(chatId,isTyping).collectLatest {
            when (it) {
                is Response.Error -> {
                    /*_allMessagesState.value = Response.Error(it.error)
                    _loadingMsgState.value = false*/
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    /* _allMessagesState.value = Response.Success(it.data)
                     _loadingMsgState.value = false*/
                }
            }
        }
    }

    internal fun listenIsTyping(chatId:String) = viewModelScope.launch {

        listenIsTypingUseCase.invoke(chatId).collectLatest {
            when (it) {
                is Response.Error -> {
                    /*_allMessagesState.value = Response.Error(it.error)
                    _loadingMsgState.value = false*/
                }

                Response.Loading -> {
                }

                is Response.Success -> {
                    _isTypingState.value = Response.Success(it.data)
                }
            }
        }
    }

    // SEND NOTIFICATION
    fun postNotification(notification: PushNotification) {
        viewModelScope.launch {
            try {
                api.postNotification(notification)
            } catch (_: Exception) {
            }
        }
    }
}