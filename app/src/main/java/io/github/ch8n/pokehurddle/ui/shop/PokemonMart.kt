package io.github.ch8n.pokehurddle.ui.shop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentMartBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartPagerAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PokemonMart : ViewBindingFragment<FragmentMartBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {

        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                labelWallet.text = "Wallet : (${it.money}) P`Coins"
            }
        }

        val adapter = MartPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        pagerItems.adapter = adapter

        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.tabTitle.get(position)
        }.attach()

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMartBinding
        get() = FragmentMartBinding::inflate
}