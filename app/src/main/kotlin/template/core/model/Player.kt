package template.core.model

import template.feature.nba.components.RowItem

data class Player(
    val id: Int,
    val teamId: Int,
    val firstName: String,
    val lastName: String,
    val team: String,
)

fun List<Player>.toRow(): List<RowItem> {
    return map { player ->
        RowItem(
            onItemClickId = player.teamId,
            titles = listOf(player.firstName, player.lastName, player.team)
        )
    }
}
