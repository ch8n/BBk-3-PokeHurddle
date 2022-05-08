package io.github.ch8n.pokehurddle.ui.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AppPagerAdapter private constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val first: Pair<String, Fragment>,
    vararg others: Pair<String, Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragments = mutableListOf(first).apply { addAll(others) }

    fun getTitle(position: Int): String = fragments.getOrNull(position)?.first ?: ""

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments.getOrNull(position)?.second ?: first.second

    companion object {
        fun newInstance(
            fragmentManager: FragmentManager,
            lifecycle: Lifecycle,
            first: Pair<String, Fragment>,
            vararg others: Pair<String, Fragment>
        ) = AppPagerAdapter(fragmentManager, lifecycle, first, *others)
    }
}