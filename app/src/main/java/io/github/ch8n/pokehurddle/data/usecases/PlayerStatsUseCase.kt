package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerStatsUseCase @Inject constructor(
    val observerPlayer: PlayerStatsObserverUseCase,
    val updatePlayerBerries: UpdatePlayerBerriesUseCase,
    val updatePlayerCoins: UpdatePlayerCoinsUseCase,
    val updatePlayerPokeballs: UpdatePlayerPokeballUseCase,
    val updatePlayerPokemon: UpdatePlayerPokemonUseCase
)

@Singleton
class PlayerStatsObserverUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    operator fun invoke(): Flow<Player> = repository.getPlayer()
}

@Singleton
class UpdatePlayerBerriesUseCase @Inject constructor(
    private val repository: PlayerRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(berry: String, qty: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = repository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedBerries = berries.toMutableMap().apply { put(berry, qty) }
                val updatedPlayer = copy(berries = updatedBerries)
                repository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerPokeballUseCase @Inject constructor(
    private val repository: PlayerRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(pokeball: String, qty: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = repository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedPokeBalls = this.pokeballs.toMutableMap().apply { put(pokeball, qty) }
                val updatedPlayer = copy(pokeballs = updatedPokeBalls)
                repository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerCoinsUseCase @Inject constructor(
    private val repository: PlayerRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(money: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = repository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedPlayer = copy(coins = money)
                repository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerPokemonUseCase @Inject constructor(
    private val repository: PlayerRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(pokemon: Pokemon) {
        userUpdateLock.withLock {
            val currentPlayerStats = repository.getPlayer().first()
            with(currentPlayerStats) {
                val currentPokemonList = this.pokemons
                val updatedPokemonList = currentPokemonList + pokemon
                val updatedPlayer = copy(pokemons = updatedPokemonList)
                repository.savePlayer(updatedPlayer)
            }
        }
    }
}