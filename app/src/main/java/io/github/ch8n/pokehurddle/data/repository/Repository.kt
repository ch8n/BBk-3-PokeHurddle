package io.github.ch8n.pokehurddle.data.repository

import io.github.ch8n.pokehurddle.data.local.sources.PlayerDAO
import io.github.ch8n.pokehurddle.data.local.sources.PokemonDAO
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.remote.PokemonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


// one instance in dependency graph
@Singleton
class PokemonRepository @Inject constructor(
    //👇 pokemon service allows to fetch remote pokemon
    private val pokemonService: PokemonService,
    //👇 pokemon DAO allows save and read pokemon from database
    private val pokemonDAO: PokemonDAO,
) {

    //👇 fetch pokemon of given id from remote server
    suspend fun fetchPokemon(id: Int) = withContext(Dispatchers.IO) {
        pokemonService.fetchPokemon(id)
    }

    //👇 read pokemon of given id from database
    suspend fun getPokemon(id: Int) = withContext(Dispatchers.IO) {
        pokemonDAO.getPokemon(id)
    }

    //👇 save pokemon of given id into the database
    suspend fun savePokemon(pokemon: Pokemon) = withContext(Dispatchers.IO) {
        pokemonDAO.savePokemon(pokemon)
    }
}

// one instance in dependency graph
@Singleton
class PlayerRepository @Inject constructor(
    //👇 player DAO allows us read/write player stats to database
    private val playerDAO: PlayerDAO
) {

    //👇 get player details in observable type
    fun getPlayer(): Flow<Player> = playerDAO.getPlayer().map {
        // if player is null then return empty player
        it.firstOrNull() ?: Player.Empty
    }.flowOn(Dispatchers.IO)

    //👇 save player details in database
    suspend fun savePlayer(player: Player) = withContext(Dispatchers.IO) {
        playerDAO.savePlayer(player)
    }
}