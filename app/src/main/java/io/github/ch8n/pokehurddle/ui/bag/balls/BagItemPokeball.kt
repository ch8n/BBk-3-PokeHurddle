package io.github.ch8n.pokehurddle.ui.bag.balls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentItemListingBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListItemAdapter
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListType
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BagItemPokeballFragment : Fragment() {

    private var binding: FragmentItemListingBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

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

    private fun setup() = with(requireNotNull(binding)) {
        val adapter = BagListItemAdapter(BagListType.POKE_BALL)
        list.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                adapter.setPlayerStats(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}