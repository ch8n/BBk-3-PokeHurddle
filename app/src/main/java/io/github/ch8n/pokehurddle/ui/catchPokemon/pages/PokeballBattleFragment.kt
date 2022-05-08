package io.github.ch8n.pokehurddle.ui.catchPokemon.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentBattleItemsBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListItemAdapter
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListType
import io.github.ch8n.pokehurddle.ui.catchPokemon.CatchPokemonFragment
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokeballBattleFragment : ViewBindingFragment<FragmentBattleItemsBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        val adapter = BagListItemAdapter(
            type = BagListType.POKE_BALL,
            onBallClicked = { pokeball ->
                if (parentFragment is CatchPokemonFragment) {
                    val catchFragment = parentFragment as CatchPokemonFragment
                    viewModel.throwBall(
                        ball = pokeball,
                        onSuccess = { catchFragment.catchSuccess(pokeball) },
                        onFailed = { "You don't have this PokeBall".snack() }
                    )
                }
            }
        )
        listBattleItems.layoutManager = LinearLayoutManager(requireContext())
        listBattleItems.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                adapter.setPlayerStats(it)
            }
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBattleItemsBinding
        get() = FragmentBattleItemsBinding::inflate

}