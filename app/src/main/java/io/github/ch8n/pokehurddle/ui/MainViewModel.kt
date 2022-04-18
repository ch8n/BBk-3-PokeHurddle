package io.github.ch8n.pokehurddle.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ch8n.pokehurddle.data.models.*
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

class MainViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private var _pokemonEncounter: PokemonDTO? = null
    val pokemonEncounter: PokemonDTO? get() = _pokemonEncounter

    private val _player = MutableLiveData(Player.Empty)
    val player: LiveData<Player> = _player

    fun updatePlayer(
        berries: Berries = Berries.Empty,
        pokeballs: Pokeballs = Pokeballs.Empty,
        pokemon: PokemonDTO = PokemonDTO.Empty,
        money: Int = 0
    ) = viewModelScope.launch(Dispatchers.IO) {

        val playerStats = _player.value ?: return@launch

        // update berries
        val previousBerryQty = playerStats.berries.get(berries) ?: 0
        val updatedBerries = if (berries !is Berries.Empty) {
            playerStats.berries.toMutableMap().apply {
                put(berries, previousBerryQty + berries.qty)
            }
        } else {
            playerStats.berries
        }

        // update pokeballs
        val previousBallsQty = playerStats.pokeballs.get(pokeballs) ?: 0
        val updatedBalls = if (pokeballs !is Pokeballs.Empty) {
            playerStats.pokeballs.toMutableMap().apply {
                put(pokeballs, previousBallsQty + 1)
            }
        } else {
            playerStats.pokeballs
        }

        // update money
        val updateMoney = playerStats.money + money

        // update pokemon
        val updatedPokemons = if (pokemon != PokemonDTO.Empty) {
            playerStats.pokemon.toMutableList().apply {
                add(pokemon)
            }
        } else {
            playerStats.pokemon
        }

        // update player stats
        val updatedStats = playerStats.copy(
            berries = updatedBerries,
            pokeballs = updatedBalls,
            pokemon = updatedPokemons,
            money = updateMoney
        )

        _player.postValue(updatedStats)
    }

    fun generateEncounter(
        onBerry: (berry: Berries) -> Unit,
        onPokeball: (berry: Pokeballs) -> Unit,
        onPokemon: (pokemon: PokemonDTO) -> Unit,
        onNothing: () -> Unit,
        onMoney: (amount: Int) -> Unit,
        onLoading: (isLoading: Boolean) -> Unit
    ) = viewModelScope.launch(Dispatchers.Main.immediate) {
        onLoading.invoke(true)
        val encounter = repository.randomEncounter
        when (encounter) {
            Encounter.Berry -> {
                delay(1000)
                onBerry.invoke(repository.randomBerry)
            }
            Encounter.Money -> {
                delay(1000)
                onMoney.invoke((encounter as Encounter.Money).amount)
            }
            Encounter.Nothing -> {
                delay(1000)
                onNothing.invoke()
            }
            Encounter.PokeBall -> {
                delay(1000)
                onPokeball.invoke(repository.randomPokeBall)
            }
            Encounter.Pokemon -> {
                val pokemonDTO = withContext(Dispatchers.IO) { repository.randomPokemon }
                if (pokemonDTO != null) {
                    _pokemonEncounter = pokemonDTO
                    onPokemon.invoke(pokemonDTO)
                } else {
                    onNothing.invoke()
                }
            }
        }
        onLoading.invoke(false)
    }

    fun onEscapePokemon(
        onLostMoney: (amount: Int) -> Unit,
        onLostBerry: (berry: Berries, amount: Int) -> Unit,
        onLostPokeball: (pokeball: Pokeballs) -> Unit,
        onEscapeNoLoss: () -> Unit,
    ) {
        val player = _player.value ?: return
        val lostAmount = (1..5).random()
        when ((0..2).random()) {
            0 -> {
                if (player.money == 0) {
                    return onEscapeNoLoss.invoke()
                }
                var amount = player.money - lostAmount
                if (amount < 0) amount = 0
                updatePlayer(money = amount)
                onLostMoney(lostAmount)
            }
            1 -> {
                val randomPlayerBerry = player.berries.toList().randomOrNull()
                    ?: return onEscapeNoLoss.invoke()
                val updatedBerryQty = max(randomPlayerBerry.second - lostAmount, 0)
                val updatedPlayerBerries = player.berries.toMutableMap().apply {
                    put(randomPlayerBerry.first, updatedBerryQty)
                }
                val updatedPlayer = player.copy(berries = updatedPlayerBerries)
                _player.postValue(updatedPlayer)
                onLostBerry(randomPlayerBerry.first, lostAmount)
            }
            2 -> {
                val randomPlayerPokeball = player.pokeballs.toList().randomOrNull()
                    ?: return onEscapeNoLoss.invoke()
                val updatedQty = max(randomPlayerPokeball.second - 1, 0)
                val updatedPlayerPokeballs = player.pokeballs.toMutableMap().apply {
                    put(randomPlayerPokeball.first, updatedQty)
                }
                val updatedPlayer = player.copy(pokeballs = updatedPlayerPokeballs)
                _player.postValue(updatedPlayer)
                onLostPokeball(randomPlayerPokeball.first)
            }
        }
    }

    private var _isEscapedFromBattleOrPet = false
    val isEscapedFromBattleOrPet get() = _isEscapedFromBattleOrPet
    fun setEscapedFromBattleOrPet(isEscaped: Boolean) {
        _isEscapedFromBattleOrPet = isEscaped
    }

    fun resetEncounterPokemon() {
        _pokemonEncounter = null
    }

}