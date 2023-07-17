package addinn.dev.team.viewModel

import addinn.dev.team.utils.connectivity.ConnectivityObserver
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectivityViewModel @Inject constructor(
    private val connection: ConnectivityObserver,
) : ViewModel() {

    var status: ConnectivityObserver.Status by mutableStateOf(ConnectivityObserver.Status.Unavailable)
        private set

    init {
        observe()
    }

    private fun observe() {
        observeConnection()
    }

    private fun observeConnection() {
        viewModelScope.launch {
            connection.observe().collectLatest { status ->
                this@ConnectivityViewModel.status = status
            }
        }
    }
}