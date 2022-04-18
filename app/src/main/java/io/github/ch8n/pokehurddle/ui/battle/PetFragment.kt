package io.github.ch8n.pokehurddle.ui.battle

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokeballs
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainActivity
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.collect


class PetFragment : Fragment() {
    private var binding: FragmentPetBinding? = null

    private var countDownTimer: CountDownTimer? = null

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


    private fun startPetTimer(onSessionOver: () -> Unit) = binding?.run {
        val ticGap = 1000L /* 1 second in millis */
        val petTime = 15 * ticGap /* 15 second Game */

        // cancel previous countdown timer
        countDownTimer?.cancel()

        // create new countdown timer
        countDownTimer = object : CountDownTimer(petTime, ticGap) {
            override fun onTick(millisUntilFinished: Long) {
                val percent = (millisUntilFinished * 100) / petTime
                progressCountdown.progress = percent.toInt()
            }

            override fun onFinish() {
                progressCountdown.progress = 0
                onSessionOver.invoke()
            }
        }

        // start countdown timer
        countDownTimer?.start()
    }

    private inline fun FragmentPetBinding.setup() {

        val pokemonInBattle = viewModel.pokemonEncounter ?: return


        lifecycleScope.launchWhenResumed {
            viewModel.player.collect {
                val player = it

                if (player == Player.Empty) {
                    return@collect
                }

                player.berries.forEach { (berry, qty) ->
                    when (berry) {
                        Berries.Empty -> {}
                        Berries.GrepaBerry -> chipBerryGrepa.text = "${berry.name} x${qty}"
                        Berries.HondewBerry -> chipBerryHondew.text = "${berry.name} x${qty}"
                        Berries.KelpsyBerry -> chipBerryKelpsy.text = "${berry.name} x${qty}"
                        Berries.PomegBerry -> chipBerryPomeg.text = "${berry.name} x${qty}"
                        Berries.QualotBerry -> chipBerryQualot.text = "${berry.name} x${qty}"
                    }
                }

                player.pokeballs.forEach { (ball, qty) ->
                    when (ball) {
                        Pokeballs.Empty -> {}
                        Pokeballs.GreatBall -> chipBallGreat.text = "${ball.name} x${qty}"
                        Pokeballs.LuxuryBall -> chipBallLuxury.text = "${ball.name} x${qty}"
                        Pokeballs.MasterBall -> chipBallMaster.text = "${ball.name} x${qty}"
                        Pokeballs.PokeBall -> chipBallPoke.text = "${ball.name} x${qty}"
                        Pokeballs.UltraBall -> chipBallUltra.text = "${ball.name} x${qty}"
                    }
                }

                val isBerriesEmpty = player.berries.values.all { it == 0 }
                if (isBerriesEmpty) {
                    countDownTimer?.onFinish()
                    containerBerries.setVisible(false)
                    containerPokeball.setVisible(true)
                }
            }
        }


        startPetTimer(
            onSessionOver = {
                containerBerries.setVisible(false)
                containerPokeball.setVisible(true)
            }
        )

        Glide.with(requireContext())
            .load(pokemonInBattle.sprites.front_default)
            .into(imgPokemon)

        labelPokemon.text = pokemonInBattle.name
        progressLove.max = pokemonInBattle.health

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            exitPetting(isPlayerExiting = true)
        }

        val berriresChip = listOf(
            chipBerryPomeg to Berries.PomegBerry,
            chipBerryKelpsy to Berries.KelpsyBerry,
            chipBerryQualot to Berries.QualotBerry,
            chipBerryHondew to Berries.HondewBerry,
            chipBerryGrepa to Berries.GrepaBerry,
        )

        berriresChip.forEach { (chip, berry) ->
            chip.setOnClickListener {
                val playerStats = viewModel.player.value
                val qty = playerStats.berries.get(berry) ?: 0
                if (qty > 0) {
                    val percent = pokemonInBattle.health / berry.attractionRate
                    updateStatus(percent)
                    berry.setQty(-1)
                    viewModel.updatePlayer(berries = berry)
                } else {
                    "you don't have this berry".toast()
                }
            }
        }

        val ballsChips = listOf(
            chipBallPoke to Pokeballs.PokeBall,
            chipBallLuxury to Pokeballs.LuxuryBall,
            chipBallGreat to Pokeballs.GreatBall,
            chipBallUltra to Pokeballs.UltraBall,
            chipBallMaster to Pokeballs.MasterBall,
        )

        ballsChips.forEach { (chip, ball) ->
            chip.setOnClickListener {
                val playerStats = viewModel.player.value
                val qty = playerStats.pokeballs.get(ball) ?: 0
                if (qty > 0) {
                    ball.qty = -1
                    viewModel.updatePlayer(pokeballs = ball)
                    catchSuccess(ball)
                } else {
                    "you don't have this ball".toast()
                }

            }
        }

    }

    private fun exitPetting(isPlayerExiting: Boolean) {
        viewModel.setEscapedFromBattleOrPet(isPlayerExiting)
        if (!isPlayerExiting) {
            viewModel.resetEncounterPokemon()
        }
        findNavController().popBackStack()
    }

    private fun FragmentPetBinding.catchSuccess(ball: Pokeballs) {
        val fillPercent = (progressLove.progress.toFloat() / progressLove.max.toFloat()) * 100
        val isCaptured = (100 - fillPercent) <= ball.successRate
        val msg = if (isCaptured) "Gotcha!!" else "Pokemon Ran away!"
        msg.toast()
        exitPetting(isPlayerExiting = false)
    }

    private fun updateStatus(percent: Int) = binding?.run {
        val likeness = randomLikeness(percent)
        val likenessMessage = if (likeness > 0) "Great! loved it..." else "Yukk.. its sour.."
        ("$likenessMessage $likeness").toString().toast()
        progressLove.progress = progressLove.progress + likeness
    }

    fun randomLikeness(amount: Int): Int {
        return when ((1..100).random()) {
            in 70..100 -> -1 * (amount / 2)
            else -> amount
        }
    }

    private var toast: Toast? = null
    fun String.toast() {
        toast?.cancel()
        toast = Toast.makeText(requireContext(), this, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        binding = null
    }
}