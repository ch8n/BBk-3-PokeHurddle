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

class ExploreViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _pokemon = MutableStateFlow<PokemonDTO?>(null)
    val pokemon = _pokemon.asStateFlow()

    private val _berry = MutableStateFlow<Berry?>(null)
    val berry = _berry.asStateFlow()

    private val _pokeball = MutableStateFlow<Pokeball?>(null)
    val pokeball = _pokeball.asStateFlow()

    private val _money = MutableStateFlow<Int>(0)
    val money = _money.asStateFlow()

    private val _nothing = MutableStateFlow<Int>(0)
    val nothing = _nothing.asStateFlow()

    fun generateEncounter() = viewModelScope.launch(Dispatchers.IO) {
        val encounter = repository.randomEncounter
        when (encounter) {
            Encounter.Berry -> {
                delay(1000)
                _berry.emit(repository.randomBerry)
            }
            Encounter.Money -> {
                delay(1000)
                _money.emit((encounter as Encounter.Money).amount)
            }
            Encounter.Nothing -> {
                delay(1000)
                _nothing.emit(_nothing.value + 1)
            }
            Encounter.PokeBall -> {
                delay(1000)
                _pokeball.emit(repository.randomPokeBall)
            }
            Encounter.Pokemon -> {
                _pokemon.emit(repository.randomPokemon)
            }
        }
    }

}