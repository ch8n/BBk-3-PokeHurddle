package io.github.ch8n.pokehurddle.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import io.github.ch8n.pokehurddle.databinding.FragmentMartBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartItemType
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartListAdapter
import kotlinx.coroutines.flow.collect


class PokemonMart : Fragment() {

    private var binding: FragmentMartBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private inline fun FragmentMartBinding.setup() {

        lifecycleScope.launchWhenResumed {
            viewModel.player.collect {
                val coins = it.money
                labelWallet.setText("Wallet : ($coins) P`Coins")
            }
        }

        val berriesAdapter = MartListAdapter(type = MartItemType.POKE_BERRY, onBerryClicked = {
            val playerCoins = viewModel.player.value.money
            if (playerCoins >= it.attractionRate) {
                viewModel.updatePlayer(berries = it, money = -(it.attractionRate))
            } else {
                "You don't have enough Poke-Coins!".toast()
            }
        })

        val pokeballAdapter = MartListAdapter(type = MartItemType.POKE_BALL, onPokeballClicked = {
            val playerCoins = viewModel.player.value.money
            if (playerCoins >= it.successRate) {
                viewModel.updatePlayer(pokeballs = it, money = -(it.successRate))
            } else {
                "You don't have enough Poke-Coins!".toast()
            }
        })

        val concatAdapter = ConcatAdapter(berriesAdapter, pokeballAdapter)
        gridMart.layoutManager = GridLayoutManager(requireContext(), 2)
        gridMart.adapter = concatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}