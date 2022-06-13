package io.github.ch8n.pokehurddle.ui.pokeMart.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentMartPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MartPokemonFragment : ViewBindingFragment<FragmentMartPokemonBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        lifecycleScope.launchWhenResumed {
            viewModel.martPokemon.collect {
                val pokemon = it

                Glide.with(requireContext())
                    .load(pokemon?.imageUrl ?: R.drawable.pokeball)
                    .into(imgPokemon)

                labelPokemonName.text = pokemon?.name ?: "Out of Service!"
                labelPokemonPrice.text = if (pokemon != null) {
                    "(${pokemon.health}) P`Coins"
                } else {
                    "No Pokemon today..."
                }

                imgPokemon.setOnClickListener {
                    if (pokemon != null) {
                        viewModel.purchasePokemon(
                            onSuccess = { "You purchased ${pokemon.name}!".snack() },
                            onFailed = { "You don't have enough Poke-Coins!".snack() }
                        )
                    } else {
                        viewModel.getPokemonOfTheDay()
                    }
                }
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMartPokemonBinding
        get() = FragmentMartPokemonBinding::inflate

}