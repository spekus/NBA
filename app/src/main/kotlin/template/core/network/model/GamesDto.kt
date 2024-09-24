package template.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import template.core.model.Game

@Serializable
data class GamesDto(
    @SerialName("data") val teams: List<GameDto>,
    @SerialName("meta") val meta: MetaDto,
)

@Serializable
data class GameDto(
    @SerialName("id") val id: Int,
    @SerialName("home_team_score") val homeScore: Int,
    @SerialName("visitor_team_score") val visitorScore: Int,
    @SerialName("home_team") val homeTeam: TeamDto,
    @SerialName("visitor_team") val visitorTeam: TeamDto
)

@Serializable
data class MetaDto(
    @SerialName("next_cursor") val nextCursor: Int = 0,
    @SerialName("per_page") val perPage: Int,
)

fun GamesDto.toModel() : List<Game> {
    return teams.map { game ->
        Game(
            id = game.id,
            homeTeam = game.homeTeam.fullName,
            visitorTeam = game.visitorTeam.fullName,
            homeScore = game.homeScore.toString(),
            visitorScore = game.visitorScore.toString()
        )
    }
}