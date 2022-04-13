package io.github.ch8n.pokehurddle.ui.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.data.models.PlayerBerry
import io.github.ch8n.pokehurddle.data.models.PlayerPokeball
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.setVisible


class ExploreFragment : Fragment() {

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

    private inline fun FragmentExploreBinding.setup() {
        btnGenerate.setOnClickListener {
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
                    val qty = berry.randomQty
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(berry.sprite)
                        .into(imgEncounter)
                    viewModel.updatePlayer(playerBerry = PlayerBerry(berry, qty))
                    labelEncounter.setText("${berry.name.capitalize()} | Qty: ${qty} | Rate: ${berry.attractionRate}")
                },
                onPokemon = {
                    val pokemon = it
                    containerPokemon.setVisible(true)
                    Glide.with(requireContext())
                        .load(pokemon.sprites.front_default)
                        .into(imgEncounter)
                    //TODO fix
                    viewModel.updatePlayer(playerPokemon = pokemon)
                    labelEncounter.setText("${pokemon.name}")
                },
                onPokeball = {
                    val pokeball = it
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(pokeball.sprite)
                        .into(imgEncounter)
                    viewModel.updatePlayer(playerPokeball = PlayerPokeball(pokeball, 1))
                    labelEncounter.setText("${pokeball.name} | Rate: ${pokeball.successRate}")
                },
                onMoney = {
                    val money = it
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.coin)
                        .into(imgEncounter)
                    viewModel.updatePlayer(playerMoney = money)
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