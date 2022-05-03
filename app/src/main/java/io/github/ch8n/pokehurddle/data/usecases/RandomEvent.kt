package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Encounter
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RandomEvent @Inject constructor(
    val getRandomPokeball: GetRandomPokeball,
    val getRandomBerry: GetRandomBerry,
    val getRandomPokemon: GetRandomPokemon,
    val getRandomEncounter: GetRandomEncounter,
    val getRandomMoney: GetRandomMoney
)

@Singleton
class GetRandomBerry @Inject constructor() {
    operator fun invoke() = flow<Berries> {
        val randomIndex = (0..100).random()
        val berry = when (randomIndex) {
            in 0..5 -> Berries.GrepaBerry
            in 5..15 -> Berries.HondewBerry
            in 15..30 -> Berries.QualotBerry
            in 30..55 -> Berries.KelpsyBerry
            else -> Berries.PomegBerry
        }
        delay(500)
        emit(berry)
    }
}

@Singleton
class GetRandomMoney @Inject constructor() {
    operator fun invoke(): Int {
        val randomCoins = (10..20).random()
        return randomCoins
    }
}


@Singleton
class GetRandomEncounter @Inject constructor() {
    operator fun invoke() = flow<Encounter> {
        val random = (0..100).random()
        val encounter = when (random) {
            in 0..10 -> Encounter.Pokemon
            in 10..20 -> Encounter.PokeBall
            in 20..30 -> Encounter.Berry
            in 30..50 -> Encounter.Money
            else -> Encounter.Nothing
        }
        emit(encounter)
    }
}

@Singleton
class GetRandomPokeball @Inject constructor() {
    operator fun invoke() = flow<Pokeball> {
        val randomIndex = (0..100).random()
        val pokeball = when (randomIndex) {
            in 0..10 -> Pokeball.MasterBall
            in 10..25 -> Pokeball.UltraBall
            in 25..45 -> Pokeball.GreatBall
            in 45..55 -> Pokeball.LuxuryBall
            else -> Pokeball.PokeBall
        }
        delay(500)
        emit(pokeball)
    }
}


@Singleton
class GetRandomPokemon @Inject constructor(
    private val appRepository: AppRepository,
) {
    operator fun invoke() = flow<PokemonDTO> {
        val randomPokemonIndex = (0..1126).random()

        val pokemon = kotlin.runCatching {
            appRepository.getPokemon(id = randomPokemonIndex)
        }.getOrNull() ?: kotlin.runCatching {
            appRepository.fetchPokemon(id = randomPokemonIndex)
        }.getOrNull()

        if (pokemon != null) {
            appRepository.savePokemon(pokemon)
            emit(pokemon)
        } else {
            error("Failed to fetch Pokemon!")
        }
    }
}
