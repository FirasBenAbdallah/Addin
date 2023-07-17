package addinn.dev.team.viewModel

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.usecase.auth.CheckUsernameUseCase
import addinn.dev.domain.usecase.auth.LoginUseCase
import addinn.dev.domain.usecase.auth.LogoutUseCase
import addinn.dev.domain.usecase.auth.RegisterUseCase
import addinn.dev.domain.usecase.auth.SubscribeToTopicUseCase
import addinn.dev.team.utils.datastore.DataStoreUtils
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val checkUsernameUseCase: CheckUsernameUseCase,
    private val sharedViewModel: SharedViewModel,
    private val subscribeToTopicUseCase: SubscribeToTopicUseCase
) : ViewModel() {

    private val isSubscribed = DataStoreUtils.readPreferenceWithoutFlow(
        key = "isSubscribed",
        defaultValue = false
    )

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    // Register
    private val _registerState = MutableStateFlow<Response<RegisterResponse>>(Response.Loading)
    val registerState: StateFlow<Response<RegisterResponse>> = _registerState

    internal fun register(imageUri: Uri, registerRequest: RegisterRequest) = viewModelScope.launch {
        _loadingState.value = true

        registerUseCase.invoke(imageUri, registerRequest).collectLatest {
            when (it) {
                is Response.Error -> {
                    _registerState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    if (isSubscribed) {
                        val user = User(
                            id = it.data.id!!,
                            email = it.data.email!!,
                            password = it.data.password!!,
                            department = it.data.department!!,
                            isOnline = it.data.isOnline!!,
                            lastSeen = it.data.lastSeen!!,
                            username = it.data.username!!,
                            avatarUrl = it.data.avatarUrl!!
                        )
                        sharedViewModel.setUser(user)
                        _registerState.value = Response.Success(it.data)
                        _loadingState.value = false
                    } else {
                        subscribeToTopicUseCase.invoke(it.data.username!!, it.data.department!!)
                            .collectLatest { data ->
                                when (data) {
                                    is Response.Error -> {
                                        _loadingState.value = false
                                    }

                                    Response.Loading -> {

                                    }

                                    is Response.Success -> {
                                        viewModelScope.launch {
                                            DataStoreUtils.savePreference(
                                                key = "isSubscribed",
                                                value = true
                                            )
                                        }
                                        _registerState.value = Response.Success(it.data)
                                        _registerState.value = Response.Success(it.data)
                    val user = User(
                        id = it.data.id,
                        email = it.data.email,
                        password = it.data.password,
                        department = it.data.department,
                    )
                    sharedViewModel.setUser(user = user)
                                        _loadingState.value = false
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    // LOGOUT
    private val _logoutState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val logoutState: StateFlow<Response<Boolean>> = _logoutState

    internal fun logout() = viewModelScope.launch {
        _loadingState.value = true

        logoutUseCase.invoke().collectLatest {
            when (it) {
                is Response.Error -> {
                    _logoutState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    //sharedViewModel.clearUser()
                    _logoutState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }

    // LOGIN
    private val _loginState = MutableStateFlow<Response<User>>(Response.Loading)
    val loginState: StateFlow<Response<User>> = _loginState

    internal fun login(loginRequest: LoginRequest) = viewModelScope.launch {
        _loadingState.value = true

        loginUseCase.invoke(loginRequest).collectLatest {
            when (it) {
                is Response.Error -> {
                    _loginState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    sharedViewModel.setUser(it.data)
                    if (isSubscribed) {
                        _loginState.value = Response.Success(it.data)
                        _loadingState.value = false
                    } else {
                        subscribeToTopicUseCase.invoke(it.data.username!!, it.data.department!!)
                            .collectLatest { data ->
                                when (data) {
                                    is Response.Error -> {
                                        _loadingState.value = false
                                    }

                                    Response.Loading -> {

                                    }

                                    is Response.Success -> {
                                        viewModelScope.launch {
                                            DataStoreUtils.savePreference(
                                                key = "isSubscribed",
                                                value = true
                                            )
                                        }
                                        _loginState.value = Response.Success(it.data)
                                        _loadingState.value = false
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    // CHECK USERNAME
    private val _loadingCheckState = MutableStateFlow(false)
    val loadingCheckState: StateFlow<Boolean> = _loadingCheckState

    // Register
    private val _checkState = MutableStateFlow<Response<Boolean>>(Response.Loading)
    val checkState: StateFlow<Response<Boolean>> = _checkState

    internal fun checkUsername(username: String) = viewModelScope.launch {
        _loadingCheckState.value = true

        checkUsernameUseCase.invoke(username).collectLatest {
            when (it) {
                is Response.Error -> {
                    _checkState.value = Response.Error(it.error)
                    _loadingCheckState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _checkState.value = Response.Success(it.data)
                    _loadingCheckState.value = false
                }
            }
        }
    }
}

