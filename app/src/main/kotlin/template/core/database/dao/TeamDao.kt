package template.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import template.core.database.model.TeamEntity

@Dao
interface TeamDao {

    @Query(value = "SELECT * FROM team")
    suspend fun getTeams(): List<TeamEntity>

    @Query(value = "SELECT * FROM team WHERE id =:id")
    fun getTeamById(id: Int): Flow<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(teams: List<TeamEntity>)
}
