package io.github.ch8n.pokehurddle.ui.bag.balls

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import io.github.ch8n.pokehurddle.databinding.FragmentItemListingBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListItemAdapter
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagListType
import kotlinx.coroutines.flow.collect

class BagItemPokeballFragment : Fragment() {

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
        val adapter = BagListItemAdapter(BagListType.POKE_BALL)
        list.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.player.collect {
                adapter.setPlayerStats(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}