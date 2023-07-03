package addinn.dev.team.utils.bottomNavigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding

@Composable
fun HomeBottomNavigation(
    bottomTab: BottomBarItem,
    setCurrentBottomTab: (BottomBarItem) -> Unit
) {
    val pages = BottomBarItem.values()

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        pages.forEach { page ->
            val selected = page == bottomTab
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = page.icon),
                        contentDescription = page.title
                    )
                },
                label = { Text(page.title) },
                alwaysShowLabel = false,
                selected = selected,
                onClick = {
                    setCurrentBottomTab.invoke(page)
                },
                modifier = Modifier.navigationBarsPadding()
            )
        }
    }
}