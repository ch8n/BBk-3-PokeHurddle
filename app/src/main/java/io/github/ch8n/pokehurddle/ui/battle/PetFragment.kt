package io.github.ch8n.pokehurddle.ui.battle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentBattleBinding
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainActivity


class PetFragment : Fragment() {
    private var binding: FragmentPetBinding? = null
    private val viewModel by lazy {
        (requireActivity() as MainActivity).sharedViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentPetBinding.setup() {

        Glide.with(requireContext())
            .load(viewModel.pokemonEncounter.sprites.front_default)
            .into(imgPokemon)

        labelPokemon.setText(
            """
            Pet --> ${viewModel.pokemonEncounter.name}
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