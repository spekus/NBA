package template.core.data.repository

import kotlinx.coroutines.flow.Flow
import template.core.common.result.Result
import template.core.model.Game
import template.core.model.Player
import template.core.model.Team

interface NbaRepository {
    fun getTeams(): Flow<Result<List<Team>>>
    fun getGames(teamId: Int): Flow<Result<List<Game>>>
    fun getPlayers(search: String): Flow<Result<List<Player>>>
}
