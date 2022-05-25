package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Berries.*
import io.github.ch8n.pokehurddle.data.models.Encounter.*
import io.github.ch8n.pokehurddle.data.models.Encounter.Nothing
import io.github.ch8n.pokehurddle.data.models.Pokeball.*
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.delay
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
    operator fun invoke() = flow {
        val randomIndex = (0..100).shuffled().first()
        val berry = when (randomIndex) {
            in 0..5 -> GrepaBerry
            in 5..15 -> HondewBerry
            in 15..30 -> QualotBerry
            in 30..55 -> KelpsyBerry
            else -> PomegBerry
        }
        emit(berry)
    }
}

@Singleton
class GetRandomMoney @Inject constructor() {
    operator fun invoke(): Int {
        val randomCoins = (5..35).shuffled().first()
        return randomCoins
    }
}


@Singleton
class GetRandomEncounter @Inject constructor() {
    operator fun invoke() = flow {
        delay(1000)
        val random = (0..100).shuffled().first()
        val encounter = when (random) {
            in 0..10 -> Pokemon
            in 10..20 -> PokeBall
            in 20..30 -> Berry
            in 30..50 -> Money
            else -> Nothing
        }
        emit(encounter)
    }
}

@Singleton
class GetRandomPokeball @Inject constructor() {
    operator fun invoke() = flow {
        val randomIndex = (0..100).shuffled().first()
        val pokeball = when (randomIndex) {
            in 0..10 -> MasterBall
            in 10..25 -> UltraBall
            in 25..45 -> GreatBall
            in 45..55 -> LuxuryBall
            else -> NormalBall
        }
        emit(pokeball)
    }
}


@Singleton
class GetRandomPokemon @Inject constructor(
    private val appRepository: AppRepository,
) {
    operator fun invoke() = flow<Pokemon> {
        val randomPokemonIndex = (0..1126).shuffled().first()

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
