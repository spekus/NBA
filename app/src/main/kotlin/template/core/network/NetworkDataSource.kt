package template.core.network

import template.core.network.model.GamesDto
import template.core.network.model.PlayersDto
import template.core.network.model.TeamsDto

interface NetworkDataSource {
    suspend fun getTeams(): TeamsDto
    suspend fun getGames(teamId: Int): GamesDto
    suspend fun getPlayers(search: String): PlayersDto
}
