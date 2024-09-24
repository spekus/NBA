package template.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import template.core.model.Player

@Serializable
data class PlayersDto(
    @SerialName("data") val players: List<PlayerBody>,
    @SerialName("meta") val meta: MetaDto,
)

@Serializable
data class PlayerBody(
    @SerialName("id") val id: Int,
    @SerialName("team") val team: TeamDto,
    @SerialName("last_name") val lastName: String,
    @SerialName("first_name") val firstName: String
)

fun PlayersDto.toModel(): List<Player> = players.map { player ->
    Player(
        id = player.id,
        teamId = player.team.id,
        firstName = player.firstName,
        lastName = player.lastName,
        team = player.team.fullName,
    )
}