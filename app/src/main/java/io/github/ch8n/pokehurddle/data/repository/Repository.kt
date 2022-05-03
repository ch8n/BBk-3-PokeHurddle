package io.github.ch8n.pokehurddle.data.repository

import io.github.ch8n.pokehurddle.data.local.sources.PlayerDAO
import io.github.ch8n.pokehurddle.data.local.sources.PokemonDAO
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.data.remote.PokemonService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val pokemonService: PokemonService,
    private val pokemonDAO: PokemonDAO,
    private val playerDAO: PlayerDAO,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun fetchPokemon(id: Int) = withContext(ioDispatcher) {
        pokemonService.fetchPokemon(id)
    }

    suspend fun getPokemon(id: Int) = withContext(ioDispatcher) {
        pokemonDAO.getPokemon(id)
    }

    suspend fun savePokemon(pokemonDTO: PokemonDTO) = withContext(ioDispatcher) {
        pokemonDAO.savePokemon(pokemonDTO)
    }

    fun getPlayer() = playerDAO.getPlayer().flowOn(ioDispatcher)

    suspend fun savePlayer(player: Player) = withContext(ioDispatcher) {
        playerDAO.savePlayer(player)
    }

}