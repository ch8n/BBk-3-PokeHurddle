package io.github.ch8n.pokehurddle.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentMartBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.shop.adapters.MartPagerAdapter
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class PokemonMart : Fragment() {

    private var binding: FragmentMartBinding? = null
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMartBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(requireNotNull(binding))
    }


    private fun setup(binding: FragmentMartBinding) = with(binding) {

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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}