package addinn.dev.team.viewModel

import addinn.dev.domain.entity.data.poll.Poll
import addinn.dev.domain.entity.data.user.User
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    val userData = mutableStateOf<User?>(null)
    private val pollData = mutableStateOf<Poll?>(null)
    fun setUserData(data: User) {
        userData.value = data
    }

    fun setPollData(data: Poll) {
        pollData.value = data
    }
}
