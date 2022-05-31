package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Berries.*
import io.github.ch8n.pokehurddle.data.models.Encounter.*
import io.github.ch8n.pokehurddle.data.models.Encounter.Nothing
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokeball.*
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.models.toPokemonEntity
import io.github.ch8n.pokehurddle.data.repository.PokemonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonWorldUseCase @Inject constructor(
    //👇 get random pokeball
    val getRandomPokeball: GetRandomPokeballUseCase,
    //👇 get random berry
    val getRandomBerry: GetRandomBerryUseCase,
    //👇 get random Pokemon
    val getRandomPokemon: GetRandomPokemonUseCase,
    //👇 get random world event
    val getRandomEncounter: GetRandomEncounterUseCase,
    //👇 get random poke coins
    val getRandomCoins: GetRandomPokeCoinsUseCase
)

@Singleton
class GetRandomBerryUseCase @Inject constructor() {

    // override invoke operator to return berry
    operator fun invoke(): Berries {
        //👇  getting random index from 0..100
        val randomIndex = (0..100).shuffled().first()

        val berry = when (randomIndex) {
            //👇 10% chance to get GrepaBerry
            in 0..10 -> GrepaBerry
            //👇 15% chance to get HondewBerry
            in 10..25 -> HondewBerry
            //👇 20% chance to get QualotBerry
            in 25..45 -> QualotBerry
            //👇 25% chance to get KelpsyBerry
            in 45..75 -> KelpsyBerry
            //👇 30% chance to get PomegBerry
            else -> PomegBerry
        }
        //👇 return random berry
        return berry
    }
}


@Singleton
class GetRandomPokeCoinsUseCase @Inject constructor() {
    // override invoke operator to return integer
    operator fun invoke(): Int {
        //👇 generate random coin
        val randomCoins = (5..35).shuffled().first()
        //👇 return coin value
        return randomCoins
    }
}

@Singleton
class GetRandomEncounterUseCase @Inject constructor() {

    //👇 override invoke operator to return Flow of Encounter
    operator fun invoke() = flow {
        // generate random value
        val random = (0..100).shuffled().first()
        val encounter = when (random) {
            //👇 10% change to encounter Pokemon
            in 0..10 -> Pokemon
            //👇 10% change to encounter PokeBall
            in 10..20 -> PokeBall
            //👇 10% change to encounter Berry
            in 20..30 -> Berry
            //👇 20% change to encounter PokeCoins
            in 30..50 -> Money
            //👇 50 % change nothing happens
            else -> Nothing
        }
        //👇 do delay to show loader
        delay(1000)
        //👇 emit encounter to flow
        emit(encounter)
    }
}

@Singleton
class GetRandomPokeballUseCase @Inject constructor() {
    operator fun invoke(): Pokeball {
        // getting random index from 0..100
        val randomIndex = (0..100).shuffled().first()
        val pokeball = when (randomIndex) {
            in 0..10 -> MasterBall // 10% chance to get masterball
            in 10..25 -> UltraBall // 15% chance to get Ultraball
            in 25..45 -> GreatBall // 20% chance to get greatball
            in 45..70 -> LuxuryBall // 25% chance to get luxuryball
            else -> NormalBall // 30% to get normal ball
        }
        return pokeball
    }
}

@Singleton
class GetRandomPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository,
) {
    //👇 override invoke operator to return Flow of Pokemon
    operator fun invoke() = flow<Pokemon> {
        //👇 generate random Pokemon Id
        val randomPokemonId = (0..1126).shuffled().first()
        val pokemon = kotlin.runCatching {
            //👇 get pokemon from database
            repository.getPokemon(id = randomPokemonId)
        }.getOrNull() ?: kotlin.runCatching {
            //👇 get pokemon from remote server
            repository.fetchPokemon(id = randomPokemonId)?.toPokemonEntity()
        }.getOrNull()

        if (pokemon != null) {
            //👇 if pokemon found, save it to database
            repository.savePokemon(pokemon)
            //👇 emit result to flow
            emit(pokemon)
        } else {
            //👇 if pokemon not found throw error
            error("Failed to fetch Pokemon!")
        }
    }
}