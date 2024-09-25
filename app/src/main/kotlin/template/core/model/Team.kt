package template.core.model

import template.feature.nba.components.RowItem

data class Team(
    val id: Int,
    val fullName: String,
    val city: String,
    val conference: String
)

fun List<Team>.toRow(): List<RowItem> {
    return map { team ->
        RowItem(
            onItemClickId = team.id,
            titles = listOf(team.fullName, team.city, team.conference)
        )
    }
}
