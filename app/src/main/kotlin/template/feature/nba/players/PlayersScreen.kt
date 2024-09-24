package template.feature.nba.players

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import template.core.model.Player
import template.core.ui.component.AppErrorSnackbar
import template.core.ui.component.AppProgressIndicator
import template.core.ui.preview.PlayersPreviewParameterProvider
import template.core.ui.preview.ThemePreviews
import template.feature.nba.components.EmptyPlaceholder
import template.feature.nba.components.PlayersList
import template.navigation.BottomNavBar
import template.navigation.Screen

@Composable
fun PlayersRoute(
    viewModel: PlayersViewModel = hiltViewModel(),
    navController: NavController,
    onPlayerClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentSelectedScreen = Screen.Players)
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                PlayersScreen(
                    uiState = uiState, onEvent = viewModel::onEvent, onItemClick = onPlayerClick
                )
            }
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayersScreen(
    uiState: PlayersUiState,
    onEvent: (PlayersUiEvent) -> Unit,
    onItemClick: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    AppErrorSnackbar(error = uiState.error,
        onErrorConsumed = { onEvent(PlayersUiEvent.ErrorConsumed) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = "Players", style = MaterialTheme.typography.headlineLarge
                )
            }
        )
        SearchSection(onSearch = { onEvent(PlayersUiEvent.Search(it)) })
        if (uiState.players.isEmpty()) {
            EmptyPlaceholder()
        } else {
            PlayersList(
                players = uiState.players,
                onItemClick = onItemClick,
                onLoadMore = { onEvent(PlayersUiEvent.LoadMore) },
            )
        }
    }

    AnimatedVisibility(
        visible = uiState.isLoading, enter = fadeIn(), exit = fadeOut()
    ) {
        AppProgressIndicator()
    }
}


@Preview(showSystemUi = true)
@ThemePreviews
@Composable
private fun PlayersScreenPreview(
    @PreviewParameter(PlayersPreviewParameterProvider::class) players: List<Player>
) {
    PlayersScreen(
        uiState = PlayersUiState(players = players),
        onEvent = {},
        onItemClick = {}
    )
}
