package io.github.ch8n.pokehurddle.ui.shop.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import io.github.ch8n.pokehurddle.databinding.FragmentItemListingBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartItemType
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartListAdapter

class MartPokeballFragment : Fragment() {

    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private var binding: FragmentItemListingBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentItemListingBinding.setup() {

        val pokeballAdapter = MartListAdapter(type = MartItemType.POKE_BALL, onPokeballClicked = {
            val playerCoins = viewModel.player.value.money
            if (playerCoins >= it.successRate) {
                "You purchased ${it.name} x1!".toast()
                viewModel.updatePlayer(pokeballs = it, money = -(it.successRate))
            } else {
                "You don't have enough Poke-Coins!".toast()
            }
        })

        list.layoutManager = GridLayoutManager(requireContext(), 2)
        list.adapter = pokeballAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}