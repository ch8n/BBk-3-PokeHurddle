package io.github.ch8n.pokehurddle.explore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import io.github.ch8n.data.repository.AppRepository
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ExploreFragment : Fragment() {

    private var binding: FragmentExploreBinding? = null
    private val repository: AppRepository = AppRepository()
    private val viewModel: ExploreViewModel = ExploreViewModel(repository)

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
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(berry.sprite)
                        .into(imgEncounter)
                    labelEncounter.setText("${berry.name.capitalize()} | Qty: ${berry.randomQty} | Rate: ${berry.attractionRate}")
                },
                onPokemon = {
                    val pokemon = it ?: return@generateEncounter
                    containerPokemon.setVisible(true)
                    Glide.with(requireContext())
                        .load(pokemon.sprites.front_default)
                        .into(imgEncounter)
                    labelEncounter.setText("${pokemon.name}")
                },
                onPokeball = {
                    val pokeball = it
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(pokeball.sprite)
                        .into(imgEncounter)
                    labelEncounter.setText("${pokeball.name} | Rate: ${pokeball.successRate}")
                },
                onMoney = {
                    val money = it
                    containerPokemon.setVisible(false)
                    Glide.with(requireContext())
                        .load(R.drawable.coin)
                        .into(imgEncounter)
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