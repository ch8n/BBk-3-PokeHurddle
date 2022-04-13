package io.github.ch8n.data.models

import java.util.*

data class Berry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val sprite: String,
    val attractionRate: Int
) {
    val randomQty: Int get() = (1..10).random()
}

val berries = listOf(
    Berry(
        name = "Pomeg-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/pomeg-berry.png",
        attractionRate = 20
    ),
    Berry(
        name = "Kelpsy-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/kelpsy-berry.png",
        attractionRate = 40
    ),
    Berry(
        name = "Qualot-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/qualot-berry.png",
        attractionRate = 60
    ),
    Berry(
        name = "Hondew-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/hondew-berry.png",
        attractionRate = 80
    ),
    Berry(
        name = "Grepa-berry",
        sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/items/grepa-berry.png",
        attractionRate = 100
    ),
)
