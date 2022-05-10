package io.github.ch8n.pokehurddle.ui.bag

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentBagBinding
import io.github.ch8n.pokehurddle.ui.bag.pages.BagItemBerriesFragment
import io.github.ch8n.pokehurddle.ui.bag.pages.BagItemPokeballFragment
import io.github.ch8n.pokehurddle.ui.utils.AppPagerAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment


@AndroidEntryPoint
class BagFragment : ViewBindingFragment<FragmentBagBinding>() {

    override fun setup() = with(binding) {
        val adapter = AppPagerAdapter.newInstance(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            "PokeBalls" to BagItemPokeballFragment(),
            "Berries" to BagItemBerriesFragment()
        )
        pagerItems.adapter = adapter

        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBagBinding
        get() = FragmentBagBinding::inflate

}