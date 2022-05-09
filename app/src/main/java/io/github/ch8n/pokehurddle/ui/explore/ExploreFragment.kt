package io.github.ch8n.pokehurddle.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull


@AndroidEntryPoint
class ExploreFragment : ViewBindingFragment<FragmentExploreBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    private fun FragmentExploreBinding.displayPokemon(pokemon: PokemonDTO) {
        containerPokemon.setVisible(true)
        btnExplore.setVisible(false)

        Glide.with(requireContext())
            .load(pokemon.sprites.front_default)
            .into(imgEncounter)

        labelEncounter.text = pokemon.name.capitalize()
    }

    private fun onEscape(message: String) = binding.run {
        containerPokemon.setVisible(false)
        imgEncounter.setImageResource(R.drawable.escape)
        labelEncounter.text = message
        loader.setVisible(false)
        btnExplore.setVisible(true)
    }

    override fun setup() = with(binding) {

        lifecycleScope.launchWhenResumed {
            viewModel.isBattleEscaped.collectLatest { isBattleEscaped ->
                if (isBattleEscaped) {
                    val pokemon = viewModel.pokemonEncountered.value
                    if (pokemon == null) {
                        onEscape("You escaped!...")
                        return@collectLatest
                    }
                    viewModel.setBattleEscaped(false)
                    onEscapingPokemon()
                }
            }
        }

        btnEscape.setOnClickListener {
            onEscapingPokemon()
        }

        btnPet.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val playerStats = viewModel.playerStats.firstOrNull() ?: return@launchWhenResumed
                val isPokeballPresent = playerStats.pokeball.values.sum() > 0
                val isBerriePresent = playerStats.berries.values.sum() > 0
                if (isPokeballPresent && isBerriePresent) {
                    findNavController().navigate(R.id.action_exploreFragment_to_petFragment)
                } else {
                    when {
                        !isPokeballPresent -> "you don't have any Pokeball".snack(btnPet)
                        !isBerriePresent -> "you don't have any berries".snack(btnPet)
                    }
                }
            }
        }

        btnExplore.setOnClickListener {
            viewModel.generateRandomEncounter(
                onNothing = {
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.nothing)
                        .into(imgEncounter)
                    labelEncounter.text = "Nothing happened!"
                },
                onBerry = { berry, qty ->
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(berry.sprite)
                        .into(imgEncounter)
                    labelEncounter.text = "You found ${berry.name.capitalize()} x ${qty}!"
                },
                onPokemon = {
                    val pokemon = it
                    displayPokemon(pokemon)
                },
                onPokeball = { pokeball ->
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(pokeball.sprite)
                        .into(imgEncounter)
                    labelEncounter.text = "You found ${pokeball.name.capitalize()} x1!"
                },
                onMoney = { coins ->
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.coin)
                        .into(imgEncounter)
                    labelEncounter.text = "You found Coins x$coins!"
                },
                onLoading = { isLoading ->
                    loader.setVisible(isLoading)
                    btnExplore.isEnabled = !isLoading
                }
            )
        }
    }

    private fun onEscapingPokemon() = with(binding) {
        imgEncounter.setImageResource(R.drawable.escape)
        labelEncounter.text = "Escaping..."
        containerPokemon.setVisible(false)
        loader.setVisible(true)
        viewModel.onPlayerEscaped(
            onLostBerry = { berry, amount ->
                onEscape("You dropped $amount ${berry.name} while escaping")
            },
            onLostMoney = {
                val qtyMsg = if (it > 0) it else "all"
                onEscape("You dropped $qtyMsg Coins while escaping")
            },
            onLostPokeball = {
                onEscape("You dropped ${it.name} while escaping")
            },
            onEscapeNoLoss = {
                onEscape("You escaped!...")
            }
        )
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentExploreBinding
        get() = FragmentExploreBinding::inflate
}