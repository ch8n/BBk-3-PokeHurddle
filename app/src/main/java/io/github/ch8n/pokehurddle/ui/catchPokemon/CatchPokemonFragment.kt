package io.github.ch8n.pokehurddle.ui.catchPokemon

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.catchPokemon.pages.BerriesBattleFragment
import io.github.ch8n.pokehurddle.ui.catchPokemon.pages.PokeballBattleFragment
import io.github.ch8n.pokehurddle.ui.utils.AppPagerAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.delay


@AndroidEntryPoint
class CatchPokemonFragment : ViewBindingFragment<FragmentPetBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {

        val pokemonInBattle = viewModel.pokemonEncountered.value ?: return

        val adapter = AppPagerAdapter.newInstance(
            fragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle,
            "Berries" to BerriesBattleFragment(),
            "PokeBalls" to PokeballBattleFragment()
        )

        pagerItems.adapter = adapter

        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        Glide.with(requireContext())
            .load(pokemonInBattle.sprites.front_default)
            .into(imgPokemon)

        labelPokemon.text = pokemonInBattle.name
        progressLove.max = pokemonInBattle.health

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

    fun catchSuccess(ball: Pokeball) = with(binding) {
        val fillPercent = (progressLove.progress.toFloat() / progressLove.max.toFloat()) * 100
        val isCaptured = (100 - fillPercent) <= ball.successRate
        val msg = if (isCaptured) "Gotcha!!" else "Pokemon Ran away!"
        msg.snack()
        lifecycleScope.launchWhenResumed {
            delay(1000)
            findNavController().popBackStack()
        }
    }

    fun updateStatus(percent: Int) = binding.run {
        val likeness = randomLikeness(percent)
        val likenessMessage = if (likeness > 0) "Great! loved it..." else "Yukk.. its sour.."
        binding.lablePokemonSpeak.text = likenessMessage
        progressLove.progress = progressLove.progress + likeness
    }

    private fun randomLikeness(amount: Int): Int {
        return when ((1..100).random()) {
            in 70..100 -> -1 * (amount / 2)
            else -> amount
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetBinding
        get() = FragmentPetBinding::inflate
}