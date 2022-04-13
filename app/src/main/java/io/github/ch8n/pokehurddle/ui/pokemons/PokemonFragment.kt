package io.github.ch8n.pokehurddle.ui.pokemons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentHealthBinding
import io.github.ch8n.pokehurddle.databinding.FragmentPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainActivity


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
        labelPokemon.setText(
            """
            pokemon :\n ${viewModel.player.pokemon.joinToString(separator = "\n")}
        """.trimIndent()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}