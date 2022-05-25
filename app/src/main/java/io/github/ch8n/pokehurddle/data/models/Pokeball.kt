package io.github.ch8n.pokehurddle.data.models

// pokehurddle/data/models/Pokeball.kt
enum class Pokeball(
    val captureRate: Int,
    val imageUrl: String,
    val price: Int,
) {

    MasterBall(
        captureRate = 80,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/master-ball.png",
        price = 180
    ),

    UltraBall(
        captureRate = 60,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/ultra-ball.png",
        price = 160
    ),

    GreatBall(
        captureRate = 40,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/great-ball.png",
        price = 140
    ),

    LuxuryBall(
        captureRate = 30,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/luxury-ball.png",
        price = 130
    ),

    NormalBall(
        captureRate = 20,
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/poke-ball.png",
        price = 120
    ),

}

