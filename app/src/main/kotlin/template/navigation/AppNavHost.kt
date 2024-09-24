package template.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import template.UserAuthState
import template.feature.launch.LaunchRoute
import template.feature.login.LoginRoute
import template.feature.nba.players.PlayersRoute
import template.feature.nba.teamdetail.TeamDetailRoute
import template.feature.nba.teams.TeamsRoute

@Serializable
sealed class NavigationGraph {
    @Serializable
    data object Login : NavigationGraph()

    @Serializable
    data object Nba : NavigationGraph()
}

@Serializable
sealed class Screen {
    @Serializable
    data object Launch : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Teams : Screen()

    @Serializable
    data object Players : Screen()

    @Serializable
    data class TeamDetails(val teamId: Int) : Screen()
}

@Suppress("LongMethod")
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    userAuth: UserAuthState
) {
    val navController = rememberNavController()

    LaunchedEffect(userAuth) {
        navController.navigate(NavigationGraph.Nba) { popUpTo(0) }
    }

    AppDeeplinkConsumer(
        isLoggedIn = userAuth is UserAuthState.LoggedIn,
        onDeeplinkConsume = { deeplink ->
            navController.navigate(deeplink)
        }
    )

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Launch
    ) {
        composable<Screen.Launch> {
            LaunchRoute()
        }

        navigation<NavigationGraph.Login>(
            startDestination = Screen.Login
        ) {
            composable<Screen.Login> {
                LoginRoute()
            }
        }

        navigation<NavigationGraph.Nba>(
            startDestination = Screen.Teams,
        ) {
            composable<Screen.Teams> {
                TeamsRoute(
                    onPostClick = { postId ->
                        navController.navigate(Screen.TeamDetails(teamId = postId))
                    },
                    navController = navController
                )
            }

            composable<Screen.Players> {
                PlayersRoute(
                    onPlayerClick = { postId ->
                        navController.navigate(Screen.TeamDetails(teamId = postId))
                    },
                    navController = navController
                )
            }

            composable<Screen.TeamDetails> {
                TeamDetailRoute(onBackClick = navController::navigateUp)
            }
        }
    }
}
