package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdatePlayerStats @Inject constructor(
    val updatePlayerBerries: UpdatePlayerBerries,
    val updatePlayerMoney: UpdatePlayerMoney,
    val updatePlayerPokeballs: UpdatePlayerPokeballs,
    val updatePlayerPokemon: UpdatePlayerPokemon
)

class UpdatePlayerBerries @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(berry: Berries, qty: Int) {
        val currentPlayerStats = appRepository.getPlayer().first()
        with(currentPlayerStats) {
            val updatedBerries = berries.toMutableMap().apply {
                put(berry, qty)
            }
            val updatedPlayer = copy(berries = updatedBerries)
            appRepository.savePlayer(updatedPlayer)
        }
    }
}

class UpdatePlayerPokeballs @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(pokeball: Pokeball, qty: Int) {
        val currentPlayerStats = appRepository.getPlayer().first()
        with(currentPlayerStats) {
            val updatedPokeBalls = this.pokeball.toMutableMap().apply {
                put(pokeball, qty)
            }
            val updatedPlayer = currentPlayerStats.copy(pokeball = updatedPokeBalls)
            appRepository.savePlayer(updatedPlayer)
        }
    }
}

class UpdatePlayerMoney @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(money: Int) {
        val currentPlayerStats = appRepository.getPlayer().first()
        with(currentPlayerStats) {
            val updatedPlayer = currentPlayerStats.copy(money = money)
            appRepository.savePlayer(updatedPlayer)
        }
    }
}

class UpdatePlayerPokemon @Inject constructor(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(pokemonDTO: PokemonDTO) {
        val currentPlayerStats = appRepository.getPlayer().first()
        with(currentPlayerStats) {
            val currentPokemonList = this.pokemon
            val updatedPokemonList = currentPokemonList + pokemonDTO
            val updatedPlayer = currentPlayerStats.copy(pokemon = updatedPokemonList)
            appRepository.savePlayer(updatedPlayer)
        }
    }
}