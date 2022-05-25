package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey val id: String,
    val berries: Map<Berries, Int>,
    val pokeballs: Map<Pokeball, Int>,
    val pokemons: List<Pokemon>,
    val money: Int
) {
    companion object {
        val Empty = Player(
            id = "",
            berries = emptyMap(),
            pokeballs = emptyMap(),
            pokemons = emptyList(),
            money = 0
        )
    }
}
