package addinn.dev.team.utils.bottomNavigation

import addinn.dev.team.R

enum class BottomBarItem(
    val title: String,
    val icon: Int
) {
    CHANNELS(
        title = "Channels",
        icon = R.drawable.outline_groups_24
    ),
    POLLS(
        title = "Polls",
        icon = R.drawable.outline_poll_24
    ),
    CHAT(
        title = "Chat",
        icon = R.drawable.outline_chat_24
    ),
    EVENTS(
        title = "Events",
        icon = R.drawable.outline_event_24
    ),
    PROFILE(
        title = "Profile",
        icon = R.drawable.outline_person_24
    );
}