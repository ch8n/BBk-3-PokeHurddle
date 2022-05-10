package io.github.ch8n.pokehurddle.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.ch8n.pokehurddle.data.models.*
import io.github.ch8n.pokehurddle.data.usecases.ObservablePlayerStats
import io.github.ch8n.pokehurddle.data.usecases.RandomEvent
import io.github.ch8n.pokehurddle.data.usecases.UpdatePlayerStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val observePlayer: ObservablePlayerStats,
    private val updatePlayerStats: UpdatePlayerStats,
    private val randomEvent: RandomEvent,
) : ViewModel() {

    val playerStats = observePlayer()

    private val _martPokemon = MutableStateFlow<PokemonDTO?>(null)
    val martPokemon = _martPokemon.asStateFlow()

    private val _isBattleEscaped = MutableStateFlow<Boolean>(false)
    val isBattleEscaped = _isBattleEscaped.asStateFlow()

    private val _pokemonEncountered = MutableStateFlow<PokemonDTO?>(null)
    val pokemonEncountered = _pokemonEncountered.asStateFlow()

    init {
        getMartPokemon()
    }

    fun getMartPokemon() {
        randomEvent.getRandomPokemon()
            .catch { e -> Log.e("Error", "failed pokemon", e) }
            .onEach { _martPokemon.emit(it) }
            .launchIn(viewModelScope)
    }

    fun generateRandomEncounter(
        onLoading: (isLoading: Boolean) -> Unit,
        onNothing: () -> Unit,
        onBerry: (berry: Berries, qty: Int) -> Unit,
        onPokeball: (berry: Pokeball) -> Unit,
        onPokemon: (pokemon: PokemonDTO) -> Unit,
        onMoney: (amount: Int) -> Unit
    ) {
        onLoading.invoke(true)
        randomEvent.getRandomEncounter()
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
        onPokemon: (pokemon: PokemonDTO) -> Unit
    ) {
        val randomPokemon =
            kotlin.runCatching { randomEvent.getRandomPokemon().first() }.getOrNull()
        val playerPokemon = playerStats.pokemon.find { it.id == randomPokemon?.id }
        if (playerPokemon != null || randomPokemon == null) {
            // player already has pokemon find new
            showPokemon(playerStats, onPokemon)
        } else {
            _pokemonEncountered.emit(randomPokemon)
            onPokemon.invoke(randomPokemon)
        }
    }

    private suspend fun addPokeball(playerStats: Player, onPokeball: (pokeball: Pokeball) -> Unit) {
        val randomPokeball = randomEvent.getRandomPokeball().first()
        val currentPokeballCount = playerStats.pokeball.get(randomPokeball) ?: 0
        updatePlayerStats.updatePlayerPokeballs(randomPokeball, currentPokeballCount + 1)
        onPokeball.invoke(randomPokeball)
    }

    private suspend fun addRandomMoney(playerStats: Player, onMoney: (amount: Int) -> Unit) {
        val currentMoney = playerStats.money
        val randomMoney = randomEvent.getRandomMoney()
        updatePlayerStats.updatePlayerMoney(currentMoney + randomMoney)
        onMoney.invoke(randomMoney)
    }

    private suspend fun addRandomBerries(
        playerStats: Player,
        onBerry: (berry: Berries, qty: Int) -> Unit
    ) {
        val randomBerry = randomEvent.getRandomBerry().first()
        val randomQty = randomBerry.getRandomQty()
        val currentQty = playerStats.berries.get(randomBerry) ?: 0
        val updatedQty = currentQty + randomQty
        updatePlayerStats.updatePlayerBerries(randomBerry, updatedQty)
        onBerry.invoke(randomBerry, randomQty)
    }

    fun onPlayerEscaped(
        onLostMoney: (amount: Int) -> Unit,
        onLostBerry: (berry: Berries, amount: Int) -> Unit,
        onLostPokeball: (pokeball: Pokeball) -> Unit,
        onEscapeNoLoss: () -> Unit,
    ) {
        randomEvent.getRandomEncounter()
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
        val (ball, currentQty) = playerStats.pokeball.entries.random()
        val lossValue = 1
        var updatedQty = currentQty - lossValue
        if (updatedQty < 0) updatedQty = 0
        updatePlayerStats.updatePlayerPokeballs(ball, updatedQty)
        onLostPokeball.invoke(ball)
    }

    private suspend fun removeMoney(playerStats: Player, onLostMoney: (amount: Int) -> Unit) {
        val currentCoins = playerStats.money
        val lossValue = (1..5).random()
        var updatedQty = currentCoins - lossValue
        if (updatedQty < 0) updatedQty = 0
        updatePlayerStats.updatePlayerMoney(updatedQty)
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
        updatePlayerStats.updatePlayerBerries(berry, updatedQty)
        onLostBerry.invoke(berry, lossValue)
    }

    fun purchaseBerry(berry: Berries, onSuccess: () -> Unit, onFailed: () -> Unit) =
        viewModelScope.launch {
            kotlin.runCatching {
                val playerStats = playerStats.first()
                if (playerStats.money >= berry.martCost) {
                    val updatedQty = (playerStats.berries.get(berry) ?: 0) + 1
                    updatePlayerStats.updatePlayerBerries(berry, updatedQty)
                    val remainingCoins = playerStats.money - berry.martCost
                    updatePlayerStats.updatePlayerMoney(money = remainingCoins)
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }.getOrElse {
                onFailed.invoke()
            }
        }

    fun purchasePokeball(pokeball: Pokeball, onSuccess: () -> Unit, onFailed: () -> Unit) =
        viewModelScope.launch {
            kotlin.runCatching {
                val playerStats = playerStats.first()
                if (playerStats.money >= pokeball.martCost) {
                    val updatedQty = (playerStats.pokeball.get(pokeball) ?: 0) + 1
                    updatePlayerStats.updatePlayerPokeballs(pokeball, updatedQty)
                    val remainingCoins = playerStats.money - pokeball.martCost
                    updatePlayerStats.updatePlayerMoney(money = remainingCoins)
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }.getOrElse {
                onFailed.invoke()
            }
        }

    fun purchasePokemon(onSuccess: () -> Unit, onFailed: () -> Unit) =
        viewModelScope.launch {
            kotlin.runCatching {
                val playerStats = playerStats.first()
                val pokemon = martPokemon.value ?: return@launch onFailed.invoke()
                if (playerStats.money >= pokemon.health) {
                    updatePlayerStats.updatePlayerPokemon(pokemon)
                    val remainingCoins = playerStats.money - pokemon.health
                    updatePlayerStats.updatePlayerMoney(money = remainingCoins)
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }.getOrElse {
                onFailed.invoke()
            }
        }

    fun throwBerry(
        berry: Berries,
        onSuccess: (percent: Int) -> Unit,
        onFailed: () -> Unit
    ) = viewModelScope.launch {
        val playerStats = playerStats.first()
        val pokemon = pokemonEncountered.value ?: return@launch onFailed.invoke()
        val qty = playerStats.berries.get(berry) ?: 0
        if (qty > 0) {
            val percent = pokemon.health / berry.attractionRate
            onSuccess.invoke(percent)
            val updatedQty = qty - 1
            updatePlayerStats.updatePlayerBerries(berry, updatedQty)
        } else {
            onFailed.invoke()
        }
    }

    fun throwBall(ball: Pokeball, onSuccess: () -> Unit, onFailed: () -> Unit) =
        viewModelScope.launch {
            delay(500)
            val playerStats = playerStats.first()
            val qty = playerStats.pokeball.get(ball) ?: 0
            if (qty > 0) {
                onSuccess.invoke()
                val updatedQty = qty - 1
                updatePlayerStats.updatePlayerPokeballs(ball, updatedQty)
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
        updatePlayerStats.updatePlayerPokemon(pokemonDTO)
    }

}