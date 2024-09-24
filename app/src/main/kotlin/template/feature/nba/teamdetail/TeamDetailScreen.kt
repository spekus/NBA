package template.feature.nba.teamdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import template.core.model.Game
import template.core.ui.preview.TeamDetailsPreviewParameterProvider
import template.core.ui.preview.ThemePreviews
import template.feature.nba.components.EmptyPlaceholder
import template.feature.nba.components.GamesList
import template.feature.nba.components.Loader

@Composable
fun TeamDetailRoute(
    viewModel: TeamDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeamDetailScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamDetailScreen(
    uiState: TeamDetailsUiState,
    onEvent: (TeamDetailsUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            },
            title = {
                Text(text = "Home")
            }
        )
        if (uiState.isLoading) {
            Loader()
        } else if (uiState.games.isEmpty()) {
            EmptyPlaceholder()
        } else {
            GamesList(
                games = uiState.games,
                onLoadMore = { onEvent(TeamDetailsUiEvent.LoadMore) }
            )
        }
    }
}

@Preview
@ThemePreviews
@Composable
private fun PostDetailScreenPreview(
    @PreviewParameter(TeamDetailsPreviewParameterProvider::class) state: List<Game>
) {
    TeamDetailScreen(
        uiState = TeamDetailsUiState(games = state),
        onBackClick = {},
        onEvent = {}
    )
}
