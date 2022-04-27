package io.github.ch8n.pokehurddle.data.models

data class Player(
    val berries: Map<Berries, Int>,
    val pokeballs: Map<Pokeballs, Int>,
    val pokemon: List<PokemonDTO>,
    val money: Int
) {
    companion object {
        val Empty = Player(
            berries = emptyMap(),
            pokeballs = emptyMap(),
            pokemon = emptyList(),
            money = 10000
        )
    }
}

