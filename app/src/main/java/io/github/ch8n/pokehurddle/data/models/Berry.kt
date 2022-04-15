package io.github.ch8n.pokehurddle.data.models

sealed class Berries(
    val name: String,
    val sprite: String,
    val attractionRate: Int
) {

    fun generateRandomBerries() {
        val amount = (1..10).random()
        _qty = amount
    }

    private var _qty: Int = 0
    val qty get() = _qty

    object PomegBerry : Berries(
        name = "Pomeg-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        attractionRate = 100
    )

    object KelpsyBerry : Berries(
        name = "Kelpsy-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        attractionRate = 80
    )

    object QualotBerry : Berries(
        name = "Qualot-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        attractionRate = 60
    )

    object HondewBerry : Berries(
        name = "Hondew-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        attractionRate = 40
    )

    object GrepaBerry : Berries(
        name = "Grepa-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        attractionRate = 20
    )

    object Empty : Berries(name = "", sprite = "", attractionRate = 0)

}