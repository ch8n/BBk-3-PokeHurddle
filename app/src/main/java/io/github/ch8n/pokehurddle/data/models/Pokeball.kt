package io.github.ch8n.pokehurddle.data.models

enum class Pokeball(
    val successRate: Int,
    val sprite: String,
    val martCost: Int,
) {

    EmptyBall(sprite = "", successRate = 0, martCost = 0),

    MasterBall(
        successRate = 80,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/master-ball.png",
        martCost = 180
    ),

    UltraBall(
        successRate = 60,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/ultra-ball.png",
        martCost = 160
    ),

    GreatBall(
        successRate = 40,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/great-ball.png",
        martCost = 140
    ),

    LuxuryBall(
        successRate = 30,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/luxury-ball.png",
        martCost = 130
    ),

    NormalBall(
        successRate = 20,
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png",
        martCost = 120
    ),

}

