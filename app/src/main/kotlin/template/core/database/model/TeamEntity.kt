package template.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "team"
)
data class TeamEntity(
    @PrimaryKey val id: Int,
    val fullName: String,
    val conference: String,
    val city: String,
)