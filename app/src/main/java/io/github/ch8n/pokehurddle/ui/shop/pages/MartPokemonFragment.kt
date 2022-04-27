package io.github.ch8n.pokehurddle.ui.shop.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentMartPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import kotlinx.coroutines.flow.collect

class MartPokemonFragment : Fragment() {

    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private var binding: FragmentMartPokemonBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMartPokemonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentMartPokemonBinding.setup() {

        lifecycleScope.launchWhenResumed {
            viewModel.martPokemon.collect {
                val pokemon = it
                if (pokemon == null) {
                    Glide.with(requireContext())
                        .load(R.drawable.pokeball)
                        .into(imgPokemon)
                    labelPokemonName.setText("Out of Service!")
                    labelPokemonPrice.setText("No Pokemon today...")
                    imgPokemon.setOnClickListener {
                        viewModel.getMartPokemon()
                    }
                    return@collect
                }

                Glide.with(requireContext())
                    .load(pokemon.sprites.front_default)
                    .into(imgPokemon)

                labelPokemonName.setText(pokemon.name)
                labelPokemonPrice.setText("(${pokemon.health}) P`Coins")

                imgPokemon.setOnClickListener {
                    val playerCoins = viewModel.player.value.money
                    if (playerCoins >= pokemon.health) {
                        "You purchased ${pokemon.name}!".toast()
                        viewModel.updatePlayer(pokemon = pokemon, money = -(pokemon.health))
                        viewModel.getMartPokemon()
                    } else {
                        "You don't have enough Poke-Coins!".toast()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}