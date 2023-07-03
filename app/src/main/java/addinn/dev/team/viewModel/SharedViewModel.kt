package addinn.dev.team.viewModel

import addinn.dev.domain.entity.user.User
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    val userData = mutableStateOf<User?>(null)

    fun setUserData(data: User) {
        userData.value = data
    }
}
