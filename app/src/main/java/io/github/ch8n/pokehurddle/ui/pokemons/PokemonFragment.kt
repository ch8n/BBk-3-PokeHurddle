package io.github.ch8n.pokehurddle.ui.pokemons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.ch8n.pokehurddle.databinding.FragmentPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import kotlinx.coroutines.flow.collect


class PokemonFragment : Fragment() {

    private var binding: FragmentPokemonBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentPokemonBinding.setup() {
        val gridAdapter = PokemonGridAdapter.newInstance()
        gridPokemon.layoutManager = GridLayoutManager(requireContext(), 2)
        gridPokemon.adapter = gridAdapter
        lifecycleScope.launchWhenResumed {
            viewModel.player.collect {
                gridAdapter.submitList(it.pokemon)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}