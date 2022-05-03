package io.github.ch8n.pokehurddle.data.models

sealed class Pokeball(
    val name: String,
    val successRate: Int,
    val sprite: String,
    val martCost: Int,
    val qty: Int = 1
) {

    object MasterBall : Pokeball(
        name = "Master-Ball",
        successRate = 80,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/master-ball.png",
        martCost = 80
    )

    object UltraBall : Pokeball(
        name = "Ultra-ball",
        successRate = 60,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/ultra-ball.png",
        martCost = 60
    )

    object GreatBall : Pokeball(
        name = "Great-ball",
        successRate = 40,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/great-ball.png",
        martCost = 40
    )

    object LuxuryBall : Pokeball(
        name = "Luxury-Ball",
        successRate = 30,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/luxury-ball.png",
        martCost = 30
    )

    object PokeBall : Pokeball(
        name = "Poke-Ball",
        successRate = 20,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png",
        martCost = 20
    )

    object Empty : Pokeball(name = "", sprite = "", successRate = 0, martCost = 0)
}
