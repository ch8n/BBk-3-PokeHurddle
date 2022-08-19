package io.github.ch8n.pokehurddle.ui.explore.catchPokemon

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
import io.github.ch8n.pokehurddle.ui.explore.catchPokemon.pages.BerriesBattleFragment
import io.github.ch8n.pokehurddle.ui.explore.catchPokemon.pages.PokeballBattleFragment
import io.github.ch8n.pokehurddle.ui.utils.AppPagerAdapter
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.delay


@AndroidEntryPoint
class CatchPokemonFragment : ViewBindingFragment<FragmentPetBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {

        // get pokemon
        val pokemonInBattle = viewModel.pokemonEncountered.value ?: kotlin.run {
            findNavController().popBackStack()
            return
        }

        // apply pager to adapter
        val adapter = AppPagerAdapter(
            // child fragment manager
            fragmentManager = childFragmentManager,
            // apply lifecycle
            lifecycle = lifecycle,
            // add fragments to pagers
            "Berries" to BerriesBattleFragment(),
            "PokeBalls" to PokeballBattleFragment()
        )

        // attach pager to adapter
        pagerItems.adapter = adapter

        // attach tabs
        TabLayoutMediator(tabs, pagerItems) { tab, position ->
            tab.text = adapter.getTitle(position)
        }.attach()

        // load image of pokemon
        Glide.with(requireContext())
            .load(pokemonInBattle.imageUrl)
            .into(imgPokemon)

        // apply pokemon name
        labelPokemon.text = pokemonInBattle.name
        // apply pokemon health
        progressHealth.max = pokemonInBattle.health

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // if user back press it will be counted as escape
            val snack = "You tried to escape from ${pokemonInBattle.name}".snack(binding.tabs)
            // set battle escape true
            viewModel.setBattleEscaped(true)
            lifecycleScope.launchWhenResumed {
                delay(1000)
                snack.dismiss()
                findNavController().popBackStack()
            }
        }
    }

    // calculate changes to catch pokemon
    fun catchSuccess(ball: Pokeball) = with(binding) {
        lifecycleScope.launchWhenResumed {
            // delay to show toast message
            delay(500)
            val fillPercent = (progressHealth.progress.toFloat() / progressHealth.max.toFloat()) * 100
            // if health is less than capture rate capture the pokemon
            val isCaptured = (100 - fillPercent) <= ball.captureRate
            val msg = if (isCaptured) "Gotcha!!" else "Pokemon Ran away!"
            // show message
            val snack = msg.snack(tabs)
            if (isCaptured) {
                // if captured save pokemon
                viewModel.captureEncounteredPokemon()
            }
            // put delay to show message
            delay(1000)
            snack.dismiss()
            // pop back to explore screen
            findNavController().popBackStack()
        }
    }

    // reduce pokemon health
    fun updatePokemonAttraction(percent: Int) = binding.run {
        // calculate likeness
        val likeness = randomLikeness(percent)
        // show message according to likeness
        val likenessMessage = if (likeness > 0) "Great! I loved it..." else "Yukk.. it's sour.."
        binding.lablePokemonSpeak.text = likenessMessage
        // update health bar
        progressHealth.progress = progressHealth.progress + likeness
    }

    // randomize likeness
    private fun randomLikeness(amount: Int): Int {
        return when ((1..100).random()) {
            in 70..100 -> -1 * (amount / 2)
            else -> amount
        }
    }

    // show snackbar over tabs
    fun showSnack(msg: String) {
        msg.snack(binding.tabs)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetBinding
        get() = FragmentPetBinding::inflate
}