package io.github.ch8n.pokehurddle.ui.battle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentBagBinding
import io.github.ch8n.pokehurddle.databinding.FragmentBattleBinding
import io.github.ch8n.pokehurddle.ui.MainActivity


class BattleFragment : Fragment() {
    private var binding: FragmentBattleBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBattleBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentBattleBinding.setup() {
        // --- TODO 10 second timer ---
        // choose pokeball
        // tap on screen to reduce health
        //

        Glide.with(requireContext())
            .load(viewModel.pokemonEncounter?.sprites?.front_default)
            .into(imgPokemon)

        labelPokemon.setText(
            """
            battle --> ${viewModel.pokemonEncounter?.name}
        """.trimIndent()
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.setEscapedFromBattleOrPet(true)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}