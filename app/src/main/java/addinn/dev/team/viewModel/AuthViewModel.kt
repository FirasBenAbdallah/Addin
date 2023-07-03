package addinn.dev.team.viewModel

import addinn.dev.domain.entity.auth.LoginRequest
import addinn.dev.domain.entity.auth.LoginResponse
import addinn.dev.domain.entity.auth.RegisterRequest
import addinn.dev.domain.entity.auth.RegisterResponse
import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.entity.user.User
import addinn.dev.domain.usecase.auth.LoginUseCase
import addinn.dev.domain.usecase.auth.LogoutUseCase
import addinn.dev.domain.usecase.auth.RegisterUseCase
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
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    // Register
    private val _registerState = MutableStateFlow<Response<RegisterResponse>>(Response.Loading)
    val registerState: StateFlow<Response<RegisterResponse>> = _registerState

    internal fun register(registerRequest: RegisterRequest) = viewModelScope.launch {
        _loadingState.value = true

        registerUseCase.invoke(registerRequest).collectLatest {
            when (it) {
                is Response.Error -> {
                    _registerState.value = Response.Error(it.error)
                    _loadingState.value = false
                }

                Response.Loading -> {

                }

                is Response.Success -> {
                    _registerState.value = Response.Success(it.data)
                    val user = User(
                        id = it.data.id,
                        email = it.data.email,
                        password = it.data.password,
                        department = it.data.department,
                    )
                    sharedViewModel.setUserData(data = user)
                    _loadingState.value = false
                }
            }
        }
    }

    // LOGIN
    private val _loginState = MutableStateFlow<Response<LoginResponse>>(Response.Loading)
    val loginState: StateFlow<Response<LoginResponse>> = _loginState

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
                    _loginState.value = Response.Success(it.data)
                    val user = User(
                        id = it.data.id,
                        email = it.data.email,
                        password = it.data.password,
                        department = it.data.department,
                    )
                    sharedViewModel.setUserData(data = user)
                    _loadingState.value = false
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
                    _logoutState.value = Response.Success(it.data)
                    _loadingState.value = false
                }
            }
        }
    }
}
