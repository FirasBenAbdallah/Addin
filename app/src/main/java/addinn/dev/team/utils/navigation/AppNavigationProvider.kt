package addinn.dev.team.utils.navigation

import addinn.dev.team.presentation.destinations.*
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigateTo

class AppNavigationProvider(
    private val navController: NavController
):NavigationProvider {
    override fun navigateToLogin() {
        navController.navigateTo(LoginViewDestination)
    }

    override fun navigateToRegister() {
        navController.navigateTo(RegisterViewDestination)
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun navigateToHome() {
        navController.navigateTo(HomeViewDestination)
    }

    override fun navigateToRecoverPass() {
        navController.navigateTo(RecoverViewDestination)
    }

    override fun navigateToResetPass() {
        navController.navigateTo(ResetViewDestination)
    override fun navigateToChat() {
        navController.navigateTo(MessagesViewDestination)
    }

    override fun navigateToMembers() {
        navController.navigateTo(MembersViewDestination)
    }
}