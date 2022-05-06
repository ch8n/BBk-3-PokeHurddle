package io.github.ch8n.pokehurddle.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonDTO(
    @PrimaryKey val id: Int,
    val name: String,
    @Embedded val sprites: Sprites,
    val stats: List<Stat>,
) {
    val attack: Int get() = stats.firstOrNull()?.base_stat ?: 0
    val health: Int get() = attack * 100

    companion object {
        val Empty = PokemonDTO(
            id = 0,
            name = "",
            sprites = Sprites("", ""),
            stats = emptyList()
        )
    }
}

@Entity
data class Sprites(
    val back_default: String,
    val front_default: String,
)

data class Stat(
    val base_stat: Int
)

