package template.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamsDto(
    @SerialName("data") val teams: List<TeamDto>,
)

@Serializable
data class TeamDto(
    @SerialName("id") val id: Int,
    @SerialName("conference") val conference: String,
    @SerialName("city") val city: String,
    @SerialName("full_name") val fullName: String
)
