package io.github.ch8n.pokehurddle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ch8n.pokehurddle.data.models.*
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private var _player = Player(
        berries = emptyList(),
        pokeballs = emptyList(),
        pokemon = emptyList(),
        money = 0
    )

    fun updatePlayer(
        playerBerry: PlayerBerry = PlayerBerry.Empty,
        playerPokeball: PlayerPokeball = PlayerPokeball.Empty,
        playerPokemon: PokemonDTO = PokemonDTO.Empty,
        playerMoney: Int = 0
    ) {
        val berries = (_player.berries + playerBerry).filter { it.qty != 0 }
        val pokeball = (_player.pokeballs + playerPokeball).filter { it.qty != 0 }
        val pokemon = (_player.pokemon + playerPokemon).filter { it.id != 0 }
        val money = _player.money + playerMoney
        _player = _player.copy(
            berries = berries,
            pokeballs = pokeball,
            pokemon = pokemon,
            money = money
        )
    }


    fun generateEncounter(
        onBerry: (berry: Berry) -> Unit,
        onPokeball: (berry: Pokeball) -> Unit,
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
                    onPokemon.invoke(pokemonDTO)
                } else {
                    onNothing.invoke()
                }
            }
        }
        onLoading.invoke(false)
    }

}