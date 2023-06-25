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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination/*(start=true)*/
@Composable
fun HomeView(navigator: NavigationProvider) {
    val (currentBottomTab, setCurrentBottomTab) = rememberSaveable {
        mutableStateOf(BottomBarItem.PROFILE)
    }

    Crossfade(currentBottomTab) { bottomTab ->
        Scaffold(
            bottomBar = { HomeBottomNavigation(bottomTab, setCurrentBottomTab) },
        ) {
            val modifier = Modifier.padding(it)
            when (bottomTab) {
                BottomBarItem.CHANNELS -> ChannelsView(navigator = navigator, modifier = modifier)
                BottomBarItem.POLLS -> PollsView(/*navigator = navigator,*/ modifier = modifier)
                BottomBarItem.CHAT -> ChatView(navigator = navigator, modifier = modifier)
                BottomBarItem.EVENTS -> EventsView(navigator = navigator)
                BottomBarItem.PROFILE -> ProfileView(/*navigator = navigator*/ modifier = modifier)
            }
        }
    }
}
