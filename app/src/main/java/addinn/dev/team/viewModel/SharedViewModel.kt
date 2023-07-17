package addinn.dev.team.viewModel

import addinn.dev.domain.entity.user.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private var currentUser by mutableStateOf(User())

    fun setUser(user: User) {
        this.currentUser = user
    }

    fun getUser(): User {
        return this.currentUser
    }

    fun clearUser() {
        this.currentUser = User()
    }
}