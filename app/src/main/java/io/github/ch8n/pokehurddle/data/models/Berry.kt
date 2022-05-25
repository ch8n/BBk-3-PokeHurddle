package io.github.ch8n.pokehurddle.data.models

// pokehurddle/data/models/Berry.kt
enum class Berries(
    val imageUrl: String,
    val tastePoints: Int,
    val price: Int,
) {
    PomegBerry(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        tastePoints = 100,
        price = 55
    ),
    KelpsyBerry(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        tastePoints = 80,
        price = 75
    ),
    QualotBerry(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        tastePoints = 60,
        price = 100
    ),
    HondewBerry(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        tastePoints = 40,
        price = 125
    ),
    GrepaBerry(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        tastePoints = 20,
        price = 150
    );

    fun getRandomQty(): Int = (1..10).random()
}


