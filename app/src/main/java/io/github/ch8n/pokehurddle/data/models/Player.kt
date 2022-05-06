package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class Player(
    @PrimaryKey val id: String,
    val berries: Map<Berries, Int>,
    val pokeball: Map<Pokeball, Int>,
    val pokemon: List<PokemonDTO>,
    val money: Int
) {
    companion object {
        val Empty = Player(
            id = "",
            berries = emptyMap(),
            pokeball = emptyMap(),
            pokemon = emptyList(),
            money = 0
        )
    }
}