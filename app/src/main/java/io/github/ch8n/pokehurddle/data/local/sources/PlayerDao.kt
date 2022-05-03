package io.github.ch8n.pokehurddle.data.local.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.ch8n.pokehurddle.data.models.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePlayer(player: Player)

    @Query("SELECT * FROM Player LIMIT 1")
    fun getPlayer(): Flow<Player>
}
