package io.github.ch8n.pokehurddle.ui.explore.catchPokemon.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentBattleItemsBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagBerriesAdapter
import io.github.ch8n.pokehurddle.ui.explore.catchPokemon.CatchPokemonFragment
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BerriesBattleFragment : ViewBindingFragment<FragmentBattleItemsBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        val adapter = BagBerriesAdapter(
            onBerryClicked = { berry ->
                if (requireParentFragment() is CatchPokemonFragment) {
                    val catchFragment = parentFragment as CatchPokemonFragment
                    viewModel.throwBerry(
                        berry = berry,
                        onSuccess = {
                            catchFragment.updateStatus(it)
                        },
                        onFailed = {
                            catchFragment.showSnack("You don't have enough berries")
                        }
                    )
                }
            }
        )
        listBattleItems.layoutManager = LinearLayoutManager(requireContext())
        listBattleItems.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                adapter.setPlayerBerries(it.berries)
            }
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBattleItemsBinding
        get() = FragmentBattleItemsBinding::inflate
}