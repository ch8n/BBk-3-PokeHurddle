package io.github.ch8n.pokehurddle.data.local.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.ch8n.pokehurddle.data.models.Pokemon

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePokemon(vararg pokemon: Pokemon)

    @Query("SELECT * FROM Pokemon WHERE Pokemon.id=:id")
    suspend fun getPokemon(id: Int): Pokemon?
}
