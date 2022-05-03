package io.github.ch8n.pokehurddle.data.models


sealed class Berries(
    val name: String,
    val sprite: String,
    val attractionRate: Int,
    val martCost: Int,
) {
    fun getRandomQty(): Int = (1..10).random()

    object PomegBerry : Berries(
        name = "Pomeg-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        attractionRate = 100,
        martCost = 40
    )

    object KelpsyBerry : Berries(
        name = "Kelpsy-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        attractionRate = 80,
        martCost = 60
    )

    object QualotBerry : Berries(
        name = "Qualot-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        attractionRate = 60,
        martCost = 80
    )

    object HondewBerry : Berries(
        name = "Hondew-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        attractionRate = 40,
        martCost = 80
    )

    object GrepaBerry : Berries(
        name = "Grepa-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        attractionRate = 20,
        martCost = 100
    )

    object Empty : Berries(name = "", sprite = "", attractionRate = 0, martCost = 0)
}

