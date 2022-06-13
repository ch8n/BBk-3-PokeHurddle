package io.github.ch8n.pokehurddle.ui.pokeMart.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentMartPokemonBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import io.github.ch8n.pokehurddle.ui.utils.setVisible

@AndroidEntryPoint
class MartPokemonFragment : ViewBindingFragment<FragmentMartPokemonBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {

        Glide.with(requireContext())
            .load(R.drawable.pokeball)
            .into(imgPokemon)
        labelPokemonPrice.text = "Loading..."
        viewModel.getPokemonOfTheDay(
            onSuccess = { pokemon ->
                Glide.with(requireContext())
                    .load(pokemon.imageUrl)
                    .into(imgPokemon)
                labelPokemonName.text = pokemon.name
                labelPokemonPrice.text = "Price : ${pokemon.health} P`Coins"
                imgPokemon.setOnClickListener {
                    viewModel.buyPokemon(
                        pokemon = pokemon,
                        onSuccess = { "You purchased ${pokemon.name}!".snack() },
                        onError = { msg -> msg.snack() }
                    )
                }
            },
            onError = {
                Glide.with(requireContext())
                    .load(R.drawable.pokeball)
                    .into(imgPokemon)
                labelPokemonName.text = "Out of Service!"
                labelPokemonPrice.setVisible(false)
                imgPokemon.setOnClickListener(null)
            }
        )
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMartPokemonBinding
        get() = FragmentMartPokemonBinding::inflate

}