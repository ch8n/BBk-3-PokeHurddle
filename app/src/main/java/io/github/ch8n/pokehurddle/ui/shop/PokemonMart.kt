package io.github.ch8n.pokehurddle.ui.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.ch8n.pokehurddle.databinding.FragmentMartBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow


class PokemonMart : Fragment() {



    // TODO features
    // central pokemon of the day! ==> health is cost
    // row 1 --> berries => muilples of 20
    // row 2 --> pokeball --> muliples of 40

    private var binding: FragmentMartBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

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
        binding?.run { setup() }
    }

    private inline fun FragmentMartBinding.setup() {
        labelMart.setText(
            """
            Mart :\n ${viewModel.player?.value?.money}
        """.trimIndent()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}