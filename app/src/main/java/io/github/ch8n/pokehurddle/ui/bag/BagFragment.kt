package io.github.ch8n.pokehurddle.ui.bag

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import io.github.ch8n.pokehurddle.databinding.FragmentBagBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagPagerAdapter


class BagFragment : Fragment() {

    private var binding: FragmentBagBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBagBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentBagBinding.setup() {
        val adapter = BagPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        pagerItems.adapter = adapter

        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.tabTitle.get(position)
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}