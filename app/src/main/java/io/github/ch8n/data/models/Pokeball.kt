package io.github.ch8n.data.models

import java.util.*

data class Pokeball(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val successRate: Int,
    val sprite: String,
)

val pokeBalls = listOf(
    Pokeball(
        name = "Master-Ball",
        successRate = 100,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/master-ball.png"
    ),
    Pokeball(
        name = "Ultra-ball",
        successRate = 80,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/ultra-ball.png"
    ),
    Pokeball(
        name = "Great-ball",
        successRate = 60,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/great-ball.png"
    ),
    Pokeball(
        name = "Luxury-Ball",
        successRate = 40,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/luxury-ball.png"
    ),
    Pokeball(
        name = "Poke-Ball",
        successRate = 20,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
    ),
).reversed()