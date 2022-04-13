package io.github.ch8n.pokehurddle.data.models

data class PokemonDTO(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val stats: List<Stat>,
) {
    val attack: Int = stats.first().base_stat
    val health: Int = attack * 100

    companion object {
        val Empty = PokemonDTO(
            id = 0,
            name = "",
            sprites = Sprites("", ""),
            stats = emptyList()
        )
    }
}

data class Sprites(
    val back_default: String,
    val front_default: String,
)

data class Stat(
    val base_stat: Int
)
