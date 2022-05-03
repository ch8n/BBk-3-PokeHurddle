package io.github.ch8n.pokehurddle.data.models

sealed class Encounter {
    object Nothing : Encounter()
    object Berry : Encounter()
    object PokeBall : Encounter()
    object Pokemon : Encounter()
    object Money : Encounter()
}

