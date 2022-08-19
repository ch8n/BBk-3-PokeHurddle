package io.github.ch8n.pokehurddle.ui.explore.catchPokemon.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.databinding.FragmentListBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.bag.adapters.BagPokeBallAdapter
import io.github.ch8n.pokehurddle.ui.explore.catchPokemon.CatchPokemonFragment
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PokeballBattleFragment : ViewBindingFragment<FragmentListBinding>() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun setup(): Unit = with(binding) {
        val adapter = BagPokeBallAdapter(
            onPokeballClicked = { pokeball ->
                if (parentFragment is CatchPokemonFragment) {
                    val catchFragment = parentFragment as CatchPokemonFragment
                    catchFragment.showSnack("You used ${pokeball.name}!")
                    viewModel.canThrowBall(
                        ball = pokeball,
                        throwBall = {
                            catchFragment.showSnack("Throwing ${pokeball.name}...")
                            catchFragment.catchSuccess(pokeball)
                        },
                        onFailed = { catchFragment.showSnack("You don't have this PokeBall") }
                    )
                }
            }
        )
        list.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                adapter.setPlayerPokeball(it.pokeballs)
            }
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentListBinding
        get() = FragmentListBinding::inflate

}