package addinn.dev.team.viewModel

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.auth.GetUserUseCase
import addinn.dev.domain.usecase.auth.SetStatusUseCase
import addinn.dev.team.presentation.destinations.HomeViewDestination
import addinn.dev.team.presentation.destinations.LoginViewDestination
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.spec.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserUseCase: GetUserUseCase,
    private val setStatusUseCase: SetStatusUseCase,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _startRoute = MutableStateFlow<Response<Route>>(Response.Loading)
    val startRoute: StateFlow<Response<Route>> = _startRoute

    init {
        checkUser()
    }

    internal fun checkUser() {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                setStatusUseCase.invoke(true, System.currentTimeMillis(), user.uid).collectLatest {
                    when (it) {
                        is Response.Error -> {
                            _startRoute.value = Response.Error(it.error)
                            _isLoading.value = false
                        }

                        Response.Loading -> {
                            _isLoading.value = true
                        }

                        is Response.Success -> {
                            getUserUseCase.invoke(user.uid).collectLatest { here ->
                                when (here) {
                                    is Response.Error -> {
                                        _startRoute.value = Response.Error(here.error)
                                        _isLoading.value = false
                                    }

                                    Response.Loading -> {
                                        _isLoading.value = true
                                    }

                                    is Response.Success -> {
                                        sharedViewModel.setUser(here.data)
                                        _startRoute.value = Response.Success(HomeViewDestination)
                                        _isLoading.value = false
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                _startRoute.value = Response.Success(LoginViewDestination)
                _isLoading.value = false
            }
        }
    }

    internal fun setOfflineStatus() {
        viewModelScope.launch {
            val user = auth.currentUser
            if (user != null) {
                setStatusUseCase.invoke(false, System.currentTimeMillis(), user.uid)
                    .collectLatest { }
            }
        }
        _isLoading.value = false
    }
}
