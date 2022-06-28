package io.github.ch8n.pokehurddle.ui.bag.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentListBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagBerriesAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BagItemBerriesFragment : ViewBindingFragment<FragmentListBinding>() {

    //👇 get shared view model
    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        //👇 create berries adapter
        val adapter = BagBerriesAdapter()
        //👇 attach list adapter to recycler view
        list.adapter = adapter
        //👇 on resumed lifecycle
        lifecycleScope.launchWhenResumed {
            //👇 from shared viewmodel collect player stats
            viewModel.playerStats.collect {
                //👇 set berries player stats to adapter
                adapter.setPlayerBerries(it.berries)
            }
        }
    }
    //👇 binding view to fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListBinding
        get() = FragmentListBinding::inflate
}