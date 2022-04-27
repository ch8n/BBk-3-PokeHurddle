package io.github.ch8n.pokehurddle.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.setVisible


class ExploreFragment : Fragment() {


    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private var binding: FragmentExploreBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

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
        binding?.run { setup() }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.isEscapedFromBattleOrPet) {
            viewModel.setEscapedFromBattleOrPet(false)
            binding?.run {
                val pokemon = viewModel.pokemonEncounter ?: return@run
                // TODO fix state not retained
                displayPokemon(pokemon)
                binding?.btnEscape?.performClick()
            }
        }
    }

    private fun FragmentExploreBinding.displayPokemon(pokemon: PokemonDTO) {
        containerPokemon.setVisible(true)
        btnExplore.setVisible(false)

        Glide.with(requireContext())
            .load(pokemon.sprites.front_default)
            .into(imgEncounter)

        labelEncounter.setText("${pokemon.name}")
    }

    fun onEscape(message: String) = binding?.run {
        containerPokemon.setVisible(false)
        imgEncounter.setImageResource(R.drawable.escape)
        labelEncounter.setText(message)
    }

    private inline fun FragmentExploreBinding.setup() {

        btnEscape.setOnClickListener {
            viewModel.onEscapePokemon(
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
            btnExplore.setVisible(true)
        }

        btnPet.setOnClickListener {
            val playerStats = viewModel.player.value
            val isPokeballPresent = playerStats.pokeballs.values.sum() > 0
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

        btnExplore.setOnClickListener {
            viewModel.generateEncounter(
                onNothing = {
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.nothing)
                        .into(imgEncounter)
                    labelEncounter.setText("Nothing happened!")
                },
                onBerry = {
                    val berry = it
                    berry.generateRandomBerries()
                    val qty = berry.qty
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(berry.sprite)
                        .into(imgEncounter)
                    viewModel.updatePlayer(berries = berry)
                    labelEncounter.setText("${berry.name.capitalize()} | Qty: ${qty} | Rate: ${berry.attractionRate}")
                },
                onPokemon = {
                    val pokemon = it
                    displayPokemon(pokemon)
                },
                onPokeball = {
                    val pokeball = it
                    pokeball.qty = 1
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(pokeball.sprite)
                        .into(imgEncounter)
                    viewModel.updatePlayer(pokeballs = pokeball)
                    labelEncounter.setText("${pokeball.name} | Rate: ${pokeball.successRate}")
                },
                onMoney = {
                    val money = it
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.coin)
                        .into(imgEncounter)
                    viewModel.updatePlayer(money = money)
                    labelEncounter.setText("Qty: ${money}")
                },
                onLoading = { isLoading ->
                    loader.setVisible(isLoading)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}