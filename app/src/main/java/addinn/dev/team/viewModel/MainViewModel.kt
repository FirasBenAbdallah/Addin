package addinn.dev.team.viewModel

import addinn.dev.domain.entity.response.Response
import addinn.dev.domain.usecase.auth.GetUserUseCase
import addinn.dev.team.presentation.NavGraphs
import addinn.dev.team.presentation.destinations.HomeViewDestination
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
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _startRoute: MutableStateFlow<Route> = MutableStateFlow(NavGraphs.root.startRoute)
    val startRoute: StateFlow<Route> = _startRoute

    init {
        checkUser()
    }

    private fun checkUser() = viewModelScope.launch {
        val user = auth.currentUser
        if (user != null) {
            _startRoute.value = HomeViewDestination
            getUserUseCase.invoke(user.uid).collectLatest {
                when (it) {
                    is Response.Error -> {
                        _isLoading.value = false
                    }

                    Response.Loading -> {}
                    is Response.Success -> {
                        sharedViewModel.setUserData(data = it.data)
                        _isLoading.value = false
                    }
                }
            }
        }
        _isLoading.value = false
    }
}
