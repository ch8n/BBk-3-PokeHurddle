package io.github.ch8n.pokehurddle.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.R
import io.github.ch8n.pokehurddle.databinding.FragmentHomeBinding
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment

@AndroidEntryPoint
class HomeFragment : ViewBindingFragment<FragmentHomeBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding =
        FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun setup() = with(binding) {

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

        imgShop.setOnClickListener {
            with(findNavController()) {
                navigate(R.id.action_homeFragment_to_pokemonMart)
            }
        }

    }
}