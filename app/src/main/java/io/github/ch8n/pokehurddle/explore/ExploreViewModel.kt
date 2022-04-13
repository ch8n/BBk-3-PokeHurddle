package io.github.ch8n.pokehurddle.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ch8n.data.models.Berry
import io.github.ch8n.data.models.Encounter
import io.github.ch8n.data.models.Pokeball
import io.github.ch8n.data.models.PokemonDTO
import io.github.ch8n.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExploreViewModel(
    private val repository: AppRepository
) : ViewModel() {

    fun generateEncounter(
        onBerry: (berry: Berry) -> Unit,
        onPokeball: (berry: Pokeball) -> Unit,
        onPokemon: (pokemon: PokemonDTO?) -> Unit,
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
                onPokemon.invoke(pokemonDTO)
            }
        }
        onLoading.invoke(false)
    }

}