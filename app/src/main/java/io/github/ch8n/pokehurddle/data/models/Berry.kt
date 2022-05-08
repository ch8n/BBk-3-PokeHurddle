package io.github.ch8n.pokehurddle.data.models

enum class Berries(
    val sprite: String,
    val attractionRate: Int,
    val martCost: Int,
) {
    PomegBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        attractionRate = 100,
        martCost = 40
    ),
    KelpsyBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        attractionRate = 80,
        martCost = 60
    ),
    QualotBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        attractionRate = 60,
        martCost = 80
    ),
    HondewBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        attractionRate = 40,
        martCost = 80
    ),
    GrepaBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        attractionRate = 20,
        martCost = 100
    ),
    EmptyBerry(sprite = "", attractionRate = 0, martCost = 0);

    fun getRandomQty(): Int = (1..10).random()
}


