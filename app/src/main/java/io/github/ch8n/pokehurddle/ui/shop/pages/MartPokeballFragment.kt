package io.github.ch8n.pokehurddle.ui.shop.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentItemListingBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartItemType
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartListAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment

@AndroidEntryPoint
class MartPokeballFragment : ViewBindingFragment<FragmentItemListingBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup() = with(binding) {
        val pokeballAdapter = MartListAdapter(
            type = MartItemType.POKE_BALL,
            onPokeballClicked = {
                viewModel.purchasePokeball(
                    pokeball = it,
                    onSuccess = { "You purchased ${it.name} x1!".snack() },
                    onFailed = { "You don't have enough Poke-Coins!".snack() }
                )
            })
        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = pokeballAdapter
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentItemListingBinding
        get() = FragmentItemListingBinding::inflate

}