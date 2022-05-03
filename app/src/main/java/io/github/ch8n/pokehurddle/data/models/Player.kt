package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity

@Entity
data class Player(
    val berries: Map<Berries, Int>,
    val pokeball: Map<Pokeball, Int>,
    val pokemon: List<PokemonDTO>,
    val money: Int
) {
    companion object {
        val Empty = Player(
            berries = emptyMap(),
            pokeball = emptyMap(),
            pokemon = emptyList(),
            money = 0
        )
    }
}

