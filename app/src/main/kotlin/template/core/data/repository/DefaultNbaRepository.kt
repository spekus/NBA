package template.core.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import template.core.common.coroutines.AppDispatchers
import template.core.common.coroutines.Dispatcher
import template.core.common.result.Result
import template.core.common.result.asResult
import template.core.data.mapper.toEntity
import template.core.data.mapper.toModel
import template.core.database.dao.TeamDao
import template.core.database.model.TeamEntity
import template.core.model.Game
import template.core.model.Player
import template.core.model.Team
import template.core.network.NetworkDataSource
import template.core.network.model.toModel
import javax.inject.Inject

class DefaultNbaRepository @Inject constructor(
    private val teamDao: TeamDao,
    private val networkDataSource: NetworkDataSource,
    @Dispatcher(AppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : NbaRepository {

    override fun getTeams(): Flow<Result<List<Team>>> {
        return flow {
            emit(getLocalTeams())
            val networkTeams = networkDataSource.getTeams()
            teamDao.insertTeams(networkTeams.toEntity())
            emit(getLocalTeams())
        }.asResult().flowOn(ioDispatcher)
    }

    private suspend fun getLocalTeams() = teamDao.getTeams().map(TeamEntity::toModel)

    override fun getGames(teamId: Int): Flow<Result<List<Game>>> {
        return flow {
            emit(networkDataSource.getGames(teamId).toModel())
        }.asResult().flowOn(ioDispatcher)
    }

    override fun getPlayers(search: String): Flow<Result<List<Player>>> {
        return flow {
            emit(networkDataSource.getPlayers(search).toModel())
        }.asResult().flowOn(ioDispatcher)
    }
}
