package addinn.dev.team.utils.navigation

interface NavigationProvider {
    fun navigateToLogin()
    fun navigateToRegister()
    fun navigateBack()
    fun navigateToHome()
    fun navigateToRecoverPass()
    fun navigateToResetPass()
    fun navigateToChat(senderId:String, receiverId:String)
    fun navigateToMembers()
    fun navigateToHelpCenter()
    fun navigateToGroupChat(usersCount:Int)
    fun navigateToNewMessage()
}