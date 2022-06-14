package io.github.ch8n.pokehurddle.ui.pokeMart.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentListBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.pokeMart.adapters.PokeballListAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment

@AndroidEntryPoint
class MartPokeballFragment : ViewBindingFragment<FragmentListBinding>() {
    //👇 get shared view model
    private val viewModel: MainViewModel by activityViewModels()

    override fun setup() = with(binding) {
        //👇 create pokemon list adapter
        val pokeballAdapter = PokeballListAdapter(
            //👇 this would be triggered when pokeball is click on adapter
            onPokeballClicked = { pokeball ->
                viewModel.buyPokeball(
                    pokeball = pokeball,
                    //👇 on purchase successful show snackbar
                    onSuccess = { "You purchased ${pokeball.name} x1!".snack() },
                    //👇 on purchase error show snackbar with error message
                    onError = { msg -> msg.snack() }
                )
            })
        //👇 apply grid layout to the recycler view
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        //👇 apply adapter to recycler view
        list.adapter = pokeballAdapter
    }

    //👇 passing inflater function to bind the UI with fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListBinding
        get() = FragmentListBinding::inflate

}