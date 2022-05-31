package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    @PrimaryKey val id: Int,
    val name: String,
    val health: Int,
    val imageUrl: String
)

data class PokemonDTO(
    val id: Int?,
    val name: String?,
    val sprites: Sprites?,
    val stats: List<Stat?>?
)

fun PokemonDTO.toPokemonEntity(): Pokemon = Pokemon(
    id = id ?: 0,
    name = name ?: "",
    health = (stats?.firstOrNull()?.base_stat ?: 0) * 100,
    imageUrl = sprites?.front_default ?: ""
)

data class Sprites(
    val back_default: String,
    val front_default: String,
)

data class Stat(
    val base_stat: Int
)

