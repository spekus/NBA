package template.feature.nba.teams

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import template.core.model.Team
import template.core.ui.component.AppErrorSnackbar
import template.core.ui.component.AppProgressIndicator
import template.core.ui.preview.TeamsPreviewParameterProvider
import template.core.ui.preview.ThemePreviews
import template.feature.nba.components.EmptyPlaceholder
import template.feature.nba.components.TeamsList
import template.feature.nba.teams.component.TeamsDialog
import template.navigation.BottomNavBar
import template.navigation.Screen


@Composable
fun TeamsRoute(
    viewModel: TeamsViewModel = hiltViewModel(),
    navController: NavController,
    onPostClick: (Int) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentSelectedScreen = Screen.Teams)
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                TeamsScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onPostClick = onPostClick
                )
            }
        }
    )
}

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamsScreen(
    uiState: TeamsUiState,
    onEvent: (TeamsUiEvent) -> Unit,
    onPostClick: (Int) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    AppErrorSnackbar(
        error = uiState.error,
        onErrorConsumed = { onEvent(TeamsUiEvent.ErrorConsumed) }
    )

    if (uiState.isDialogVisible) {
        TeamsDialog(
            onDismissRequest = { onEvent(TeamsUiEvent.CloseDialog) },
            onSelect = { onEvent(TeamsUiEvent.Sort(it)) },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        TopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            actions = {
                Button(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    onClick = { onEvent(TeamsUiEvent.OpenDialog) }
                ) {
                    Text(text = uiState.buttonText.toString())
                }
            }
        )

        if (uiState.teams.isEmpty()) {
            EmptyPlaceholder()
        } else {
            TeamsList(
                teams = uiState.teams,
                onItemClick = onPostClick,
            )
        }
    }

    AnimatedVisibility(
        visible = uiState.isLoading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AppProgressIndicator()
    }
}

@Preview(showSystemUi = true)
@ThemePreviews
@Composable
private fun PostsScreenPreview(
    @PreviewParameter(TeamsPreviewParameterProvider::class)
    teams: List<Team>
) {
    TeamsScreen(
        uiState = TeamsUiState(teams = teams),
        onEvent = {},
        onPostClick = {}
    )
}
