package io.github.ch8n.pokehurddle.data.models

data class PlayerBerry(
    val berry: Berry,
    val qty: Int,
) {
    companion object {
        val Empty = PlayerBerry(Berry.Empty, 0)
    }
}

data class PlayerPokeball(
    val pokeball: Pokeball,
    val qty: Int,
) {
    companion object {
        val Empty = PlayerPokeball(Pokeball.Empty, 0)
    }
}

data class Player(
    val berries: List<PlayerBerry>,
    val pokeballs: List<PlayerPokeball>,
    val pokemon: List<PokemonDTO>,
    val money: Int
)

