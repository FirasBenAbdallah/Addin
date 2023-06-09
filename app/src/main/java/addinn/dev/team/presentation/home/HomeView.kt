package addinn.dev.team.presentation.home

import addinn.dev.team.presentation.channels.ChannelsView
import addinn.dev.team.presentation.chat.ChatView
import addinn.dev.team.presentation.events.EventsView
import addinn.dev.team.presentation.polls.PollsView
import addinn.dev.team.presentation.profile.ProfileView
import addinn.dev.team.utils.bottomNavigation.BottomBarItem
import addinn.dev.team.utils.bottomNavigation.HomeBottomNavigation
import addinn.dev.team.utils.navigation.NavigationProvider
import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun HomeView(navigator: NavigationProvider) {
    val (currentBottomTab, setCurrentBottomTab) = rememberSaveable {
        mutableStateOf(BottomBarItem.CHAT)
    }

    Crossfade(currentBottomTab) { bottomTab ->
        Scaffold(
            bottomBar = { HomeBottomNavigation(bottomTab, setCurrentBottomTab) },
        ) {
            when (bottomTab) {
                BottomBarItem.CHANNELS -> ChannelsView(navigator = navigator)
                BottomBarItem.POLLS -> PollsView(navigator = navigator)
                BottomBarItem.CHAT -> ChatView(navigator = navigator)
                BottomBarItem.EVENTS -> EventsView(navigator = navigator)
                BottomBarItem.PROFILE -> ProfileView(navigator = navigator)
            }
        }
    }
}