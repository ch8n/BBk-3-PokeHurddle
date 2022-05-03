package io.github.ch8n.pokehurddle.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.firstOrNull


@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private var binding: FragmentExploreBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun FragmentExploreBinding.displayPokemon(pokemon: PokemonDTO) {
        containerPokemon.setVisible(true)
        btnExplore.setVisible(false)

        Glide.with(requireContext())
            .load(pokemon.sprites.front_default)
            .into(imgEncounter)

        labelEncounter.text = pokemon.name.capitalize()
    }

    fun onEscape(message: String) = binding?.run {
        containerPokemon.setVisible(false)
        imgEncounter.setImageResource(R.drawable.escape)
        labelEncounter.text = message
        btnExplore.setVisible(true)
    }

    private fun setup() = with(requireNotNull(binding)) {

        btnEscape.setOnClickListener {
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

        btnPet.setOnClickListener {
            lifecycleScope.launchWhenResumed {
                val playerStats = viewModel.playerStats.firstOrNull() ?: return@launchWhenResumed
                val isPokeballPresent = playerStats.pokeball.values.sum() > 0
                val isBerriePresent = playerStats.berries.values.sum() > 0
                if (isPokeballPresent && isBerriePresent) {
                    findNavController().navigate(R.id.action_exploreFragment_to_petFragment)
                } else {
                    when {
                        !isPokeballPresent -> "you don't have any Pokeball".toast()
                        !isBerriePresent -> "you don't have any berries".toast()
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
                    labelEncounter.setText("Nothing happened!")
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}