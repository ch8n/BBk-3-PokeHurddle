package io.github.ch8n.pokehurddle.ui.pokeMart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentMartBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.pokeMart.pages.MartBerriesFragment
import io.github.ch8n.pokehurddle.ui.pokeMart.pages.MartPokeballFragment
import io.github.ch8n.pokehurddle.ui.pokeMart.pages.MartPokemonFragment
import io.github.ch8n.pokehurddle.ui.utils.AppPagerAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PokeMartFragment : ViewBindingFragment<FragmentMartBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {

        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                labelWallet.text = "Wallet : (${it.coins}) P`Coins"
            }
        }

        val adapter = AppPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            "Pokemon Of Day" to MartPokemonFragment(),
            "Berries" to MartBerriesFragment(),
            "PokeBalls" to MartPokeballFragment()
        )

        pagerItems.adapter = adapter

        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMartBinding
        get() = FragmentMartBinding::inflate
}