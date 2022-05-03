package io.github.ch8n.pokehurddle.data.local.sources

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.ch8n.pokehurddle.data.models.PokemonDTO

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePokemon(vararg pokemonDTO: PokemonDTO)

    @Query("SELECT * FROM PokemonDTO WHERE PokemonDTO.id=:id")
    suspend fun getPokemon(id: Int): PokemonDTO?
}
