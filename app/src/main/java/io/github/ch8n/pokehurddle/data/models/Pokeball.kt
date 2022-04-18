package io.github.ch8n.pokehurddle.data.models

sealed class Pokeballs(
    val name: String,
    val successRate: Int,
    val sprite: String,
) {
    var qty = 1

    object MasterBall : Pokeballs(
        name = "Master-Ball",
        successRate = 80,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/master-ball.png"
    )

    object UltraBall : Pokeballs(
        name = "Ultra-ball",
        successRate = 60,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/ultra-ball.png"
    )

    object GreatBall : Pokeballs(
        name = "Great-ball",
        successRate = 40,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/great-ball.png"
    )

    object LuxuryBall : Pokeballs(
        name = "Luxury-Ball",
        successRate = 30,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/luxury-ball.png"
    )

    object PokeBall : Pokeballs(
        name = "Poke-Ball",
        successRate = 20,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png"
    )

    object Empty : Pokeballs(name = "", sprite = "", successRate = 0)
}
