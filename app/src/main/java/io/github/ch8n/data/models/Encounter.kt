package io.github.ch8n.data.models

sealed class Encounter {
    object Nothing : Encounter()
    object Berry : Encounter()
    object PokeBall : Encounter()
    object Pokemon : Encounter()
    object Money : Encounter() {
        val amount: Int get() = (1..10).random()
    }
}
