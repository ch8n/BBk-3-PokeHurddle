package io.github.ch8n.pokehurddle.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AppPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    firstFragment: Pair<String, Fragment>,
    vararg otherFragments: Pair<String, Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = mutableListOf(firstFragment).apply { addAll(otherFragments) }

    fun getTitle(position: Int): String = fragments
        .getOrElse(position) { fragments.first() }
        .first

    override fun createFragment(position: Int): Fragment = fragments
        .getOrElse(position) { fragments.first() }
        .second

    override fun getItemCount(): Int = fragments.size
}