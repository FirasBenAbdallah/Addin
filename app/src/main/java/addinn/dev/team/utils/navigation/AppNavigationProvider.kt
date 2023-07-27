package addinn.dev.team.utils.navigation

import addinn.dev.team.presentation.destinations.AddEventViewDestination
import addinn.dev.team.presentation.destinations.GroupChatViewDestination
import addinn.dev.team.presentation.destinations.HelpCenterDestination
import addinn.dev.team.presentation.destinations.HomeViewDestination
import addinn.dev.team.presentation.destinations.LoginViewDestination
import addinn.dev.team.presentation.destinations.MembersViewDestination
import addinn.dev.team.presentation.destinations.MessagesViewDestination
import addinn.dev.team.presentation.destinations.NewMessageViewDestination
import addinn.dev.team.presentation.destinations.PrivateGroupsChatViewDestination
import addinn.dev.team.presentation.destinations.PrivateGroupsViewDestination
import addinn.dev.team.presentation.destinations.RecoverViewDestination
import addinn.dev.team.presentation.destinations.RegisterViewDestination
import addinn.dev.team.presentation.destinations.ResetViewDestination
import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigateTo
import com.ramcosta.composedestinations.navigation.popUpTo

class AppNavigationProvider(
    private val navController: NavController
) : NavigationProvider {
    override fun navigateToLogin() {
        navController.navigateTo(direction = LoginViewDestination, navOptionsBuilder = {
            popUpTo(HomeViewDestination) {
                inclusive = true
            }
        })
    }

    override fun navigateToRegister() {
        navController.navigateTo(RegisterViewDestination)
    }

    override fun navigateBack() {
        navController.navigateUp()
    }

    override fun navigateToHome() {
        navController.navigateTo(direction = HomeViewDestination, navOptionsBuilder = {
            popUpTo(HomeViewDestination) {
                inclusive = true
            }
        })
    }

    override fun navigateToRecoverPass() {
        navController.navigateTo(RecoverViewDestination)
    }

    override fun navigateToResetPass() {
        navController.navigateTo(ResetViewDestination)
    }

    override fun navigateToChat(
        senderId: String,
        receiverId: String
    ) {
        navController.navigateTo(
            MessagesViewDestination(
                senderUsername = senderId,
                receiverId = receiverId,
            )
        )
    }

    override fun navigateToMembers() {
        navController.navigateTo(MembersViewDestination)
    }

    override fun navigateToHelpCenter() {
        navController.navigateTo(HelpCenterDestination)
    }
    
    override fun navigateToGroupChat(usersCount: Int) {
        navController.navigateTo(GroupChatViewDestination(usersCount))
    }

    override fun navigateToNewMessage() {
        navController.navigateTo(NewMessageViewDestination)
    }

    override fun navigateToPrivateGroups() {
        navController.navigateTo(PrivateGroupsViewDestination)
    }

    override fun navigateToPrivateGroupsChat(usersCount: Int,
                                             groupId: String,) {
        navController.navigateTo(PrivateGroupsChatViewDestination(usersCount = usersCount, groupId = groupId))
    }

    override fun navigateToAddEvent() {
        navController.navigateTo(AddEventViewDestination)
    }
}