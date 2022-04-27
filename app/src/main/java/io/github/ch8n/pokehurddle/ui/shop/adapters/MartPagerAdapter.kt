package io.github.ch8n.pokehurddle.ui.shop.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.ch8n.pokehurddle.ui.shop.pages.MartBerriesFragment
import io.github.ch8n.pokehurddle.ui.shop.pages.MartPokeballFragment
import io.github.ch8n.pokehurddle.ui.shop.pages.MartPokemonFragment

class MartPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val tabTitle = listOf("Pokemon Of Day", "Poke-Berries", "Poke-balls")

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> MartPokemonFragment()
        1 -> MartBerriesFragment()
        else -> MartPokeballFragment()
    }
}