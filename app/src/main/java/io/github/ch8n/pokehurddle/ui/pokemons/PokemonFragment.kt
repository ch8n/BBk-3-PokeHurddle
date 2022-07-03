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

    // get shared view model
    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        // create grid adapter
        val gridAdapter = PokemonGridAdapter()
        // apply layout manager
        gridPokemon.layoutManager = GridLayoutManager(requireContext(), 2)
        // apply grid adapter to recycler view
        gridPokemon.adapter = gridAdapter
        // onResumed lifecycle
        lifecycleScope.launchWhenResumed {
            // collect player stats
            viewModel.playerStats.collect {
                // pass pokemon object into grid adapter
                gridAdapter.submitList(it.pokemons)
            }
        }
    }

    // bind layout to fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPokemonBinding
        get() = FragmentPokemonBinding::inflate

}