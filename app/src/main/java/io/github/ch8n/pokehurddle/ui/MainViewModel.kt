package io.github.ch8n.pokehurddle.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ch8n.pokehurddle.data.models.*
import io.github.ch8n.pokehurddle.data.usecases.PlayerStatsUseCase
import io.github.ch8n.pokehurddle.data.usecases.PokemonMartUseCase
import io.github.ch8n.pokehurddle.data.usecases.PokemonWorldUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerStatsUseCase: PlayerStatsUseCase,
    private val pokemonWorldUseCase: PokemonWorldUseCase,
    private val pokemartUseCase: PokemonMartUseCase,
) : ViewModel() {

    // player status observer usecase
    val playerStats = playerStatsUseCase.observerPlayer()

    // delegate to fetch pokemon of the day usecase
    fun getPokemonOfTheDay(
        // callback for operation success
        onSuccess: (pokemon: Pokemon) -> Unit,
        // callback for operation error
        onError: (msg: String) -> Unit
    ) {
        pokemartUseCase.getPokemonOfTheDay()
            // on error trigger onError callback
            .catch { error -> onError.invoke(error.message ?: "Something went wrong!") }
            // on Each trigger success callback
            .onEach { pokemon -> onSuccess.invoke(pokemon) }
            // start flow on viewmodel scope
            .launchIn(viewModelScope)
    }

    // delegate to purchase pokemon usecase
    fun buyPokemon(pokemon: Pokemon, onSuccess: () -> Unit, onError: (msg: String) -> Unit) =
        viewModelScope.launch {
            pokemartUseCase.purchasePokemon(
                pokemon = pokemon,
                onSuccess = onSuccess,
                onError = onError
            )
        }

    // delegate to purchase pokeball usecase
    fun buyPokeball(pokeball: Pokeball, onSuccess: () -> Unit, onError: (msg: String) -> Unit) =
        viewModelScope.launch {
            pokemartUseCase.purchasePokeball(
                pokeball = pokeball,
                onSuccess = onSuccess,
                onError = onError
            )
        }

    // delegate to purchase berry usecase
    fun buyBerry(berry: Berries, onSuccess: () -> Unit, onError: (msg: String) -> Unit) =
        viewModelScope.launch {
            pokemartUseCase.purchaseBerry(
                berry = berry,
                onSuccess = onSuccess,
                onError = onError
            )
        }

    private val _isBattleEscaped = MutableStateFlow<Boolean>(false)
    val isBattleEscaped = _isBattleEscaped.asStateFlow()

    private val _pokemonEncountered = MutableStateFlow<Pokemon?>(null)
    val pokemonEncountered = _pokemonEncountered.asStateFlow()

    fun generateRandomEncounter(
        onLoading: (isLoading: Boolean) -> Unit,
        onNothing: () -> Unit,
        onBerry: (berry: Berries, qty: Int) -> Unit,
        onPokeball: (berry: Pokeball) -> Unit,
        onPokemon: (pokemon: Pokemon) -> Unit,
        onMoney: (amount: Int) -> Unit
    ) {
        onLoading.invoke(true)
        pokemonWorldUseCase.getRandomEncounter()
            .onEach { encounter ->
                val playerStats = playerStats.first()
                when (encounter) {
                    Encounter.Berry -> addRandomBerries(playerStats, onBerry)
                    Encounter.Money -> addRandomMoney(playerStats, onMoney)
                    Encounter.Nothing -> onNothing()
                    Encounter.PokeBall -> addPokeball(playerStats, onPokeball)
                    Encounter.Pokemon -> showPokemon(playerStats, onPokemon)
                }
            }
            .catch { onNothing.invoke() }
            .onCompletion { onLoading.invoke(false) }
            .launchIn(viewModelScope)
    }

    private tailrec suspend fun showPokemon(
        playerStats: Player,
        onPokemon: (pokemon: Pokemon) -> Unit
    ) {
        val randomPokemon =
            kotlin.runCatching { pokemonWorldUseCase.getRandomPokemon().first() }.getOrNull()
        val playerPokemon = playerStats.pokemons.find { it.id == randomPokemon?.id }
        if (playerPokemon != null || randomPokemon == null) {
            // player already has pokemon find new
            showPokemon(playerStats, onPokemon)
        } else {
            _pokemonEncountered.emit(randomPokemon)
            onPokemon.invoke(randomPokemon)
        }
    }

    private suspend fun addPokeball(playerStats: Player, onPokeball: (pokeball: Pokeball) -> Unit) {
        val randomPokeball = pokemonWorldUseCase.getRandomPokeball()
        val currentPokeballCount = playerStats.pokeballs.get(randomPokeball.name) ?: 0
        playerStatsUseCase.updatePlayerPokeballs(randomPokeball.name, currentPokeballCount + 1)
        onPokeball.invoke(randomPokeball)
    }

    private suspend fun addRandomMoney(playerStats: Player, onMoney: (amount: Int) -> Unit) {
        val currentMoney = playerStats.coins
        val randomMoney = pokemonWorldUseCase.getRandomCoins()
        playerStatsUseCase.updatePlayerCoins(currentMoney + randomMoney)
        onMoney.invoke(randomMoney)
    }

    private suspend fun addRandomBerries(
        playerStats: Player,
        onBerry: (berry: Berries, qty: Int) -> Unit
    ) {
        val randomBerry = pokemonWorldUseCase.getRandomBerry()
        val randomQty = randomBerry.getRandomQty()
        val currentQty = playerStats.berries.get(randomBerry.name) ?: 0
        val updatedQty = currentQty + randomQty
        playerStatsUseCase.updatePlayerBerries(randomBerry.name, updatedQty)
        onBerry.invoke(randomBerry, randomQty)
    }

    fun onPlayerEscaped(
        onLostMoney: (amount: Int) -> Unit,
        onLostBerry: (berry: Berries, amount: Int) -> Unit,
        onLostPokeball: (pokeball: Pokeball) -> Unit,
        onEscapeNoLoss: () -> Unit,
    ) {
        pokemonWorldUseCase.getRandomEncounter()
            .onEach { encounter ->
                val playerStats = playerStats.first()
                when (encounter) {
                    Encounter.Berry -> removeBerry(playerStats, onLostBerry)
                    Encounter.Money -> removeMoney(playerStats, onLostMoney)
                    Encounter.Nothing -> onEscapeNoLoss.invoke()
                    Encounter.PokeBall -> removePokeball(playerStats, onLostPokeball)
                    Encounter.Pokemon -> onEscapeNoLoss.invoke()
                }
            }
            .catch { onEscapeNoLoss.invoke() }
            .onCompletion { onEscapeNoLoss.invoke() }
            .launchIn(viewModelScope)
    }

    private suspend fun removePokeball(
        playerStats: Player,
        onLostPokeball: (pokeball: Pokeball) -> Unit
    ) {
        val (ball, currentQty) = playerStats.pokeballs.entries.random()
        val lossValue = 1
        var updatedQty = currentQty - lossValue
        if (updatedQty < 0) updatedQty = 0
        playerStatsUseCase.updatePlayerPokeballs(ball, updatedQty)
        onLostPokeball.invoke(Pokeball.valueOf(ball))
    }

    private suspend fun removeMoney(playerStats: Player, onLostMoney: (amount: Int) -> Unit) {
        val currentCoins = playerStats.coins
        val lossValue = (1..5).random()
        var updatedQty = currentCoins - lossValue
        if (updatedQty < 0) updatedQty = 0
        playerStatsUseCase.updatePlayerCoins(updatedQty)
        onLostMoney.invoke(lossValue)
    }

    private suspend fun removeBerry(
        playerStats: Player,
        onLostBerry: (berry: Berries, amount: Int) -> Unit
    ) {
        val (berry, currentQty) = playerStats.berries.entries.random()
        val lossValue = (1..5).random()
        var updatedQty = currentQty - lossValue
        if (updatedQty < 0) updatedQty = 0
        playerStatsUseCase.updatePlayerBerries(berry, updatedQty)
        onLostBerry.invoke(Berries.valueOf(berry), lossValue)
    }


    fun throwBerry(
        berry: Berries,
        onSuccess: (percent: Int) -> Unit,
        onFailed: () -> Unit
    ) = viewModelScope.launch {
        val playerStats = playerStats.first()
        val pokemon = pokemonEncountered.value ?: return@launch onFailed.invoke()
        val qty = playerStats.berries.get(berry.name) ?: 0
        if (qty > 0) {
            val percent = pokemon.health / berry.tastePoints
            onSuccess.invoke(percent)
            val updatedQty = qty - 1
            playerStatsUseCase.updatePlayerBerries(berry.name, updatedQty)
        } else {
            onFailed.invoke()
        }
    }

    fun throwBall(ball: Pokeball, onSuccess: () -> Unit, onFailed: () -> Unit) =
        viewModelScope.launch {
            delay(500)
            val playerStats = playerStats.first()
            val qty = playerStats.pokeballs.get(ball.name) ?: 0
            if (qty > 0) {
                onSuccess.invoke()
                val updatedQty = qty - 1
                playerStatsUseCase.updatePlayerPokeballs(ball.name, updatedQty)
            } else {
                onFailed.invoke()
            }
        }

    fun setBattleEscaped(hasEscaped: Boolean) = viewModelScope.launch {
        _isBattleEscaped.emit(hasEscaped)
        if (!hasEscaped) {
            _pokemonEncountered.emit(null)
        }
    }

    fun captureEncounteredPokemon() = viewModelScope.launch {
        val pokemonDTO = pokemonEncountered.value ?: return@launch
        playerStatsUseCase.updatePlayerPokemon(pokemonDTO)
    }

}