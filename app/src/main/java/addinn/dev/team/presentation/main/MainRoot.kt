package addinn.dev.team.presentation.main

import addinn.dev.team.presentation.NavGraphs
import addinn.dev.team.presentation.destinations.DirectionDestination
import addinn.dev.team.presentation.destinations.HomeViewDestination
import addinn.dev.team.utils.navigation.AppNavigationProvider
import addinn.dev.team.utils.theme.TeamTheme
import addinn.dev.team.viewModel.MainViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.spec.Route

@Composable
fun MainRoot(finish: () -> Unit, startRoute: Route = NavGraphs.root.startRoute) {
    val navController = rememberNavController()

    val currentBackStackEntryAsState by navController.currentBackStackEntryAsState()
    val destination = currentBackStackEntryAsState?.destination?.route
        ?: NavGraphs.root.startRoute.route

    if (destination == NavGraphs.root.startRoute.route) {
        BackHandler { finish() }
    }

    TeamTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            DestinationsNavHost(
                navController = navController,
                navGraph = NavGraphs.root,
                startRoute = startRoute,
                dependenciesContainerBuilder = {
                    dependency(AppNavigationProvider(navController))
                }
            )
        }
    }
}