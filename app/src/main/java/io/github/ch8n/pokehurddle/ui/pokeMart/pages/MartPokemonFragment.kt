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
    //👇 get shared view model from activity
    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        //👇 load place holder till pokemon of the day is loading
        Glide.with(requireContext())
            .load(R.drawable.pokeball)
            .into(imgPokemon)
        //👇 set price label to loading
        labelPokemonPrice.text = "Loading..."
        //👇 reset any click listener on image
        imgPokemon.setOnClickListener(null)

        //👇 trigger fetching of pokemon of the day
        viewModel.getPokemonOfTheDay(
            //👇 when pokemon is fetched succesfully
            onSuccess = { pokemon ->
                //👇 load image from URL and set on the image view
                Glide.with(requireContext())
                    .load(pokemon.imageUrl)
                    .into(imgPokemon)
                //👇 set pokemon name
                labelPokemonName.text = pokemon.name
                //👇 set pokemon price
                val price = pokemon.health / 2
                labelPokemonPrice.text = "Buy?: Only P`Coins $price/- "
                //👇 on click trigger purchase of pokemon
                imgPokemon.setOnClickListener {
                    viewModel.buyPokemon(
                        pokemon = pokemon,
                        //👇 on purchase success show snackbar message
                        onSuccess = { "You purchased ${pokemon.name}!".snack() },
                        //👇 on  error show snackbar message with error
                        onError = { msg -> msg.snack() }
                    )
                }
            },
            // when pokemon is fetched failed
            onError = { msg ->
                //👇 load place holder image
                Glide.with(requireContext())
                    .load(R.drawable.pokeball)
                    .into(imgPokemon)
                //👇 show error message
                labelPokemonName.text = "Out of Service!"
                labelPokemonPrice.text = msg
                //👇 remove click listener
                imgPokemon.setOnClickListener(null)
            }
        )
    }

    //👇 passing inflater function to bind the UI with fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMartPokemonBinding
        get() = FragmentMartPokemonBinding::inflate

}