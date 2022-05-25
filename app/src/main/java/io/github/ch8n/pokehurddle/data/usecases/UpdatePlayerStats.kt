package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePlayerStats @Inject constructor(
    val updatePlayerBerries: UpdatePlayerBerries,
    val updatePlayerMoney: UpdatePlayerMoney,
    val updatePlayerPokeballs: UpdatePlayerPokeballs,
    val updatePlayerPokemon: UpdatePlayerPokemon
)

@Singleton
class UpdatePlayerBerries @Inject constructor(
    private val appRepository: AppRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(berry: Berries, qty: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = appRepository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedBerries = berries.toMutableMap().apply { put(berry, qty) }
                val updatedPlayer = copy(berries = updatedBerries)
                appRepository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerPokeballs @Inject constructor(
    private val appRepository: AppRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(pokeball: Pokeball, qty: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = appRepository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedPokeBalls = this.pokeballs.toMutableMap().apply { put(pokeball, qty) }
                val updatedPlayer = copy(pokeballs = updatedPokeBalls)
                appRepository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerMoney @Inject constructor(
    private val appRepository: AppRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(money: Int) {
        userUpdateLock.withLock {
            val currentPlayerStats = appRepository.getPlayer().first()
            with(currentPlayerStats) {
                val updatedPlayer = copy(money = money)
                appRepository.savePlayer(updatedPlayer)
            }
        }
    }
}

@Singleton
class UpdatePlayerPokemon @Inject constructor(
    private val appRepository: AppRepository,
    private val userUpdateLock: Mutex
) {
    suspend operator fun invoke(pokemon: Pokemon) {
        userUpdateLock.withLock {
            val currentPlayerStats = appRepository.getPlayer().first()
            with(currentPlayerStats) {
                val currentPokemonList = this.pokemons
                val updatedPokemonList = currentPokemonList + pokemon
                val updatedPlayer = copy(pokemons = updatedPokemonList)
                appRepository.savePlayer(updatedPlayer)
            }
        }
    }
}