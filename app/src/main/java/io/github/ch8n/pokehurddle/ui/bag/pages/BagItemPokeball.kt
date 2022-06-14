package io.github.ch8n.pokehurddle.ui.bag.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentListBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListItemAdapter
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListType
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BagItemPokeballFragment : ViewBindingFragment<FragmentListBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        val adapter = BagListItemAdapter(BagListType.POKE_BALL)
        list.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                adapter.setPlayerStats(it)
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListBinding
        get() = FragmentListBinding::inflate
}