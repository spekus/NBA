package template.core.model

import template.feature.nba.components.RowItem

data class Game(
    val id: Int,
    val homeTeam: String,
    val visitorTeam: String,
    val homeScore: String,
    val visitorScore: String,
)

fun List<Game>.toRow(): List<RowItem> {
    return map { game ->
        RowItem(
            onItemClickId = game.id,
            titles = listOf(game.homeTeam, game.homeScore, game.visitorTeam, game.visitorScore)
        )
    }
}
