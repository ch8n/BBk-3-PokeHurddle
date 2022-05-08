package io.github.ch8n.pokehurddle.ui.pokemons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PokemonFragment : ViewBindingFragment<FragmentPokemonBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        val gridAdapter = PokemonGridAdapter.newInstance()
        gridPokemon.layoutManager = GridLayoutManager(requireContext(), 2)
        gridPokemon.adapter = gridAdapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                gridAdapter.submitList(it.pokemon)
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPokemonBinding
        get() = FragmentPokemonBinding::inflate

}