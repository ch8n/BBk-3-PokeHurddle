package io.github.ch8n.pokehurddle.ui.bag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentBagBinding
import io.github.ch8n.pokehurddle.databinding.FragmentExploreBinding
import io.github.ch8n.pokehurddle.ui.MainActivity


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
        labelItem.setText(
            """
            berries :\n ${viewModel.player.value?.berries}
            -----
            -----
            pokeballs:\n ${viewModel.player.value?.pokeballs}
        """.trimIndent()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}