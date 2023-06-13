package addinn.dev.team.utils.navigation

interface NavigationProvider {
    fun navigateToLogin()
    fun navigateToRegister()
    fun navigateBack()
    fun navigateToHome()
    fun navigateToRecoverPass()
    fun navigateToResetPass()
}