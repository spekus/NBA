package template.feature.nba.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import template.core.model.Game
import template.core.model.Player
import template.core.model.Team
import template.feature.nba.InfiniteListHandler

@Composable
fun TeamsList(
    modifier: Modifier = Modifier, teams: List<Team>, onItemClick: (Int) -> Unit
) {
    Labels(listOf("Name", "City", "Conference"))
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(count = teams.size) { index ->
            RowItem(columns = listOf(
                teams[index].fullName, teams[index].city, teams[index].conference
            ), onItemClick = { onItemClick(teams[index].id) })
        }
    }
}

@Composable
fun PlayersList(
    modifier: Modifier = Modifier,
    players: List<Player>,
    onItemClick: (Int) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    Labels(listOf("Fist Name", "Last Name", "Team"))
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(count = players.size) { index ->
            RowItem(columns = listOf(
                players[index].firstName,
                players[index].lastName,
                players[index].team,
            ), onItemClick = { onItemClick(players[index].teamId) })
        }
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}

@Composable
fun GamesList(
    modifier: Modifier = Modifier, games: List<Game>, onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    Labels(listOf("Home Name", "Home Score", "Visitor Name", "Visitor Score"))
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        state = listState
    ) {
        items(count = games.size) { index ->
            RowItem(
                columns = listOf(
                    games[index].homeTeam,
                    games[index].homeScore,
                    games[index].visitorTeam,
                    games[index].visitorScore,
                ),
            )
        }
    }

    InfiniteListHandler(listState = listState) {
        onLoadMore()
    }
}


@Composable
fun Labels(labels: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        labels.forEach { label ->
            Text(
                label,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Start
            )
        }
    }
}
