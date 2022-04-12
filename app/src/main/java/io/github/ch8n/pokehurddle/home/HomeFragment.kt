package io.github.ch8n.pokehurddle.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.run { setup() }
    }

    private inline fun FragmentHomeBinding.setup() {
        imgExplore.setOnClickListener {
            with(findNavController()) {
                navigate(R.id.action_homeFragment_to_exploreFragment)
            }
        }

        imgBag.setOnClickListener {
            with(findNavController()) {
                navigate(R.id.action_homeFragment_to_bagFragment)
            }
        }

        imgPokemon.setOnClickListener {
            with(findNavController()) {
                navigate(R.id.action_homeFragment_to_pokemonFragment)
            }
        }

        imgPokecenter.setOnClickListener {
            with(findNavController()) {
                navigate(R.id.action_homeFragment_to_recoverFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}