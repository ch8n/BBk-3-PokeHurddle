package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val berries: Map<String, Int>,
    val pokeballs: Map<String, Int>,
    val pokemons: List<Pokemon>,
    val coins: Int
) {
    companion object {
        val Empty = Player(
            id = 0,
            berries = emptyMap(),
            pokeballs = emptyMap(),
            pokemons = emptyList(),
            coins = 0
        )
    }
}
