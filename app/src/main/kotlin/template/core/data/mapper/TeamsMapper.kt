package template.core.data.mapper

import template.core.database.model.TeamEntity
import template.core.model.Team
import template.core.network.model.TeamsDto

fun TeamsDto.toEntity() =
    this.teams.map { team ->
        with(team) {
            TeamEntity(
                id = id,
                fullName = fullName,
                conference = conference,
                city = city
            )
        }
}

fun TeamEntity.toModel() = Team(
    id = this.id,
    fullName = this.fullName,
    conference = this.conference,
    city = this.city
)