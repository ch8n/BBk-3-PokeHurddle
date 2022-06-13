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
    val getPokemonOfTheDay: GetPokemonOfTheDayUseCase,
    val purchasePokemon: PurchasePokemonUseCase,
    val purchasePokeball: PurchasePokeballUseCase,
    val purchaseBerry: PurchaseBerriesUseCase,
)

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
                val player = playerStats.observerPlayer().firstOrNull()
                if (player == null) {
                    onError.invoke("Something went wrong!")
                    return@withContext
                }
                if (player.coins < pokeball.price) {
                    onError("You don't have enough Poke-Coins!")
                    return@withContext
                }
                val updatedQty = (player.pokeballs.get(pokeball.name) ?: 0) + 1
                playerStats.updatePlayerPokeballs(pokeball.name, updatedQty)
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
class PurchasePokemonUseCase @Inject constructor(
    private val userUpdateLock: Mutex,
    private val playerStats: PlayerStatsUseCase,
) {
    suspend operator fun invoke(
        pokemon: Pokemon,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    ) = withContext(NonCancellable) {
        try {
            userUpdateLock.withLock {
                val playerCoins = playerStats.observerPlayer().firstOrNull()?.coins
                    ?: return@withContext onError.invoke("Something went wrong!")
                if (playerCoins >= pokemon.health) {
                    val remainingCoins = playerCoins - pokemon.health
                    playerStats.updatePlayerCoins(remainingCoins)
                    playerStats.updatePlayerPokemon(pokemon)
                    onSuccess.invoke()
                } else {
                    onError("You don't have enough Poke-Coins!")
                }
            }
        } catch (error: Exception) {
            Log.e("Error", error.message, error)
            onError.invoke("Something went wrong!")
        }
    }
}


@Singleton
class GetPokemonOfTheDayUseCase @Inject constructor(
    private val getRandomPokemon: GetRandomPokemonUseCase
) {
    private val _martPokemon = MutableStateFlow<Pokemon?>(null)

    operator fun invoke(): Flow<Pokemon> = flow {
        var pokemon = _martPokemon.value
        if (pokemon != null) {
            emit(pokemon)
            return@flow
        }
        pokemon = getRandomPokemon().firstOrNull()
        if (pokemon != null) {
            _martPokemon.value = pokemon
            emit(pokemon)
            return@flow
        }
        error("Failed to fetch Pokemon...")
    }
}
