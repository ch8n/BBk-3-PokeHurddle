package io.github.ch8n.pokehurddle.data.models

enum class Berries(
    val sprite: String,
    val attractionRate: Int,
    val martCost: Int,
) {
    PomegBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        attractionRate = 100,
        martCost = 140
    ),
    KelpsyBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        attractionRate = 80,
        martCost = 160
    ),
    QualotBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        attractionRate = 60,
        martCost = 180
    ),
    HondewBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        attractionRate = 40,
        martCost = 200
    ),
    GrepaBerry(
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        attractionRate = 20,
        martCost = 225
    ),
    EmptyBerry(sprite = "", attractionRate = 0, martCost = 0);

    fun getRandomQty(): Int = (1..10).random()
}


