package io.github.ch8n.pokehurddle.data.usecases

import android.util.Log
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokemon
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonMartUseCase @Inject constructor(
    //👇 Fetch Pokemon of the Day
    val getPokemonOfTheDay: GetPokemonOfTheDayUseCase,
    //👇 Purchase Pokemon and update player stats
    val purchasePokemon: PurchasePokemonUseCase,
    //👇 Purchase Pokeball and update player stats
    val purchasePokeball: PurchasePokeballUseCase,
    //👇 Purchase Berry and update player stats
    val purchaseBerry: PurchaseBerriesUseCase,
)

@Singleton
class GetPokemonOfTheDayUseCase @Inject constructor(
    private val getRandomPokemon: GetRandomPokemonUseCase
) {
    //👇 We keep a flow that would cache the last pokemon result
    private val _martPokemon = MutableStateFlow<Pokemon?>(null)

    operator fun invoke(): Flow<Pokemon> = flow {
        //👇 get pokemon from cache
        var pokemon = _martPokemon.value
        if (pokemon != null) {
            //👇 if found emit pokemon and return
            emit(pokemon)
            return@flow
        }
        //👇 if pokemon not found then fetch new pokemon
        pokemon = getRandomPokemon().firstOrNull()
        if (pokemon != null) {
            //👇 if pokemon fetched then cache pokemon
            // emit to flow then return
            _martPokemon.value = pokemon
            emit(pokemon)
            return@flow
        }
        //👇 else error has happened emit error to flow
        error("Failed to fetch Pokemon...")
    }
}


@Singleton
class PurchasePokemonUseCase @Inject constructor(
    private val userUpdateLock: Mutex,
    private val playerStats: PlayerStatsUseCase,
) {
    suspend operator fun invoke(
        //👇 pokemon we want to purchase
        pokemon: Pokemon,
        //👇 purchase success callback
        onSuccess: () -> Unit,
        //👇 purchase failed callback
        onError: (message: String) -> Unit
    ) = withContext(NonCancellable) {
        //👇 we wrap entire operation in try/catch to handle unknown errors
        try {
            //👇 lock to protect data corruption
            userUpdateLock.withLock {
                //👇 get player coins
                val playerCoins = playerStats.observerPlayer().firstOrNull()?.coins
                //👇 error if player coins failed to get
                    ?: return@withContext onError.invoke("Something went wrong!")

                //👇 calculate pokemon price
                if (playerCoins < pokemon.price) {
                    //👇 if player coins has less coins than pokemon price
                    // then error and return
                    onError("You don't have enough Poke-Coins!")
                    return@withContext
                }
                //👇 if player has coins then calculate remaining coins
                val remainingCoins = playerCoins - pokemon.price
                //👇 update player coins and pokemon
                playerStats.updatePlayerCoins(remainingCoins)
                playerStats.updatePlayerPokemon(pokemon)
                onSuccess.invoke()
            }
        } catch (error: Exception) {
            //👇 if unexpected error occured then invoke error flow
            Log.e("Error", error.message, error)
            onError.invoke("Something went wrong!")
        }
    }
}

@Singleton
class PurchasePokeballUseCase @Inject constructor(
    private val userUpdateLock: Mutex,
    private val playerStats: PlayerStatsUseCase,
) {
    suspend operator fun invoke(
        pokeball: Pokeball,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) = withContext(NonCancellable) {
        try {
            userUpdateLock.withLock {
                //👇 get player stats
                val player = playerStats.observerPlayer().firstOrNull()
                if (player == null) {
                    //👇 if failed to fetch player stats show error
                    onError.invoke("Something went wrong!")
                    return@withContext
                }
                if (player.coins < pokeball.price) {
                    //👇 player doesn't have enough coins show error
                    onError("You don't have enough Poke-Coins!")
                    return@withContext
                }
                //👇 update player pokeball quanitity
                val updatedQty = (player.pokeballs.get(pokeball.name) ?: 0) + 1
                playerStats.updatePlayerPokeballs(pokeball.name, updatedQty)
                //👇 update players coins
                val remainingCoins = player.coins - pokeball.price
                playerStats.updatePlayerCoins(money = remainingCoins)
                onSuccess.invoke()
            }
        } catch (error: Exception) {
            Log.e("Error", error.message, error)
            onError.invoke("Something went wrong!")
        }
    }
}


@Singleton
class PurchaseBerriesUseCase @Inject constructor(
    private val userUpdateLock: Mutex,
    private val playerStats: PlayerStatsUseCase,
) {
    suspend operator fun invoke(
        berry: Berries,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) = withContext(NonCancellable) {
        try {
            userUpdateLock.withLock {
                val player = playerStats.observerPlayer().firstOrNull()
                if (player == null) {
                    onError.invoke("Something went wrong!")
                    return@withContext
                }
                if (player.coins < berry.price) {
                    onError("You don't have enough Poke-Coins!")
                    return@withContext
                }
                val updatedQty = (player.berries.get(berry.name) ?: 0) + 1
                playerStats.updatePlayerBerries(berry.name, updatedQty)
                val remainingCoins = player.coins - berry.price
                playerStats.updatePlayerCoins(money = remainingCoins)
                onSuccess.invoke()
            }
        } catch (error: Exception) {
            Log.e("Error", error.message, error)
            onError.invoke("Something went wrong!")
        }
    }
}




