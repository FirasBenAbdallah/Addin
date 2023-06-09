package addinn.dev.team.utils.navigation

import addinn.dev.team.presentation.destinations.HomeViewDestination
import addinn.dev.team.presentation.destinations.LoginViewDestination
import addinn.dev.team.presentation.destinations.RegisterViewDestination
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
}