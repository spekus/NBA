package template.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import template.core.model.Game
import template.core.model.Player
import template.core.model.Team

class TeamsPreviewParameterProvider : PreviewParameterProvider<List<Team>> {
    override val values: Sequence<List<Team>>
        get() = sequenceOf(
            listOf(
                Team(
                    id = 1,
                    fullName = "Zalgiris",
                    city = "Vilnius",
                    conference = "Europe"
                )
            )
        )
}

class PlayersPreviewParameterProvider : PreviewParameterProvider<List<Player>> {
    override val values: Sequence<List<Player>>
        get() = sequenceOf(
            listOf(
                Player(
                    id = 1,
                    teamId = 1,
                    firstName = "Augustas",
                    lastName = "Vilnietis",
                    team = "Rytas"
                )
            )
        )
}

class TeamDetailsPreviewParameterProvider : PreviewParameterProvider<List<Game>> {
    override val values: Sequence<List<Game>>
        get() = sequenceOf(
            listOf(
                Game(
                    id = 1,
                    homeTeam = "Rytas",
                    visitorTeam = "Zalgiris",
                    homeScore = "123",
                    visitorScore = "102"
                )
            )
        )
}
