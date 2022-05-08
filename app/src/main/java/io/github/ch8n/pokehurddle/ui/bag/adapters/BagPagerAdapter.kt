package io.github.ch8n.pokehurddle.ui.bag.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.github.ch8n.pokehurddle.ui.bag.pages.BagItemPokeballFragment
import io.github.ch8n.pokehurddle.ui.bag.pages.BagItemBerriesFragment

class BagPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val tabTitle = listOf("Poke-Balls", "Poke-Berries")

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> BagItemPokeballFragment()
        else -> BagItemBerriesFragment()
    }
}