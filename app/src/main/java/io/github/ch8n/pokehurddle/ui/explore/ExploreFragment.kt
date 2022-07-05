package io.github.ch8n.pokehurddle.ui.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import io.github.ch8n.pokehurddle.ui.utils.setVisible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import java.util.*


@AndroidEntryPoint
class ExploreFragment : ViewBindingFragment<FragmentExploreBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    private fun onLoading(isLoading: Boolean) = with(binding) {
        loader.setVisible(isLoading)
        btnExplore.isEnabled = !isLoading
    }

    private fun onNothingHappened(): Unit = with(binding) {
        containerPokemon.setVisible(false)
        Glide.with(requireContext())
            .load(R.drawable.nothing)
            .into(imgEncounter)
        labelEncounter.text = "Nothing happened!"
    }

    private fun onPokeCoinsFound(coins: Int): Unit = with(binding) {
        containerPokemon.setVisible(false)
        Glide.with(requireContext())
            .load(R.drawable.coin)
            .into(imgEncounter)
        labelEncounter.text = "You found Coins x$coins!"
    }

    private fun onPokeballFound(pokeball: Pokeball): Unit = with(binding) {
        containerPokemon.setVisible(false)
        Glide.with(requireContext())
            .load(pokeball.imageUrl)
            .into(imgEncounter)
        labelEncounter.text = "You found ${pokeball.name.capitalize()} x1!"
    }


    private fun onPokemonFound(pokemon: Pokemon) = with(binding) {
        containerPokemon.setVisible(true)
        btnExplore.setVisible(false)
        Glide.with(requireContext())
            .load(pokemon.imageUrl)
            .into(imgEncounter)
        labelEncounter.text = pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    private fun onBerryFound(berry: Berries, qty: Int): Unit = with(binding) {
        containerPokemon.setVisible(false)
        Glide.with(requireContext())
            .load(berry.imageUrl)
            .into(imgEncounter)
        labelEncounter.text = "You found ${berry.name.capitalize()} x${qty}!"
    }

    private fun onEscape(message: String) = with(binding) {
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

        btnEscape.setOnClickListener { onEscapingPokemon() }

        btnPet.setOnClickListener {
            viewModel.isReadyForBattle(
                onReady = {
                    findNavController().navigate(R.id.action_exploreFragment_to_petFragment)
                },
                onError = { msg -> msg.snack(btnPet) }
            )
        }

        btnExplore.setOnClickListener {
            viewModel.generateRandomEncounter(
                onNothing = { onNothingHappened() },
                onBerry = { berry, qty -> onBerryFound(berry, qty) },
                onPokemon = { pokemon -> onPokemonFound(pokemon) },
                onPokeball = { pokeball -> onPokeballFound(pokeball) },
                onMoney = { coins -> onPokeCoinsFound(coins) },
                onLoading = { isLoading -> onLoading(isLoading) }
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