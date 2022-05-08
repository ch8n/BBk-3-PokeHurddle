package io.github.ch8n.pokehurddle.ui.catchPokemon

import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.data.models.Berries.*
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokeball.*
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.pokehurddle.ui.utils.ViewBindingFragment
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CatchPokemonFragment : ViewBindingFragment<FragmentPetBinding>() {

    private var countDownTimer: CountDownTimer? = null
    private val viewModel: MainViewModel by activityViewModels()

    private fun startPetTimer(onSessionOver: () -> Unit) = binding.run {
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

    override fun setup() = with(binding) {

        val pokemonInBattle = viewModel.pokemonEncountered.value ?: return

        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
                val player = it

                if (player == Player.Empty) {
                    return@collect
                }

                player.berries.forEach { (berry, qty) ->
                    when (berry) {
                        EmptyBerry -> {}
                        GrepaBerry -> chipBerryGrepa.text = "${berry.name} x${qty}"
                        HondewBerry -> chipBerryHondew.text = "${berry.name} x${qty}"
                        KelpsyBerry -> chipBerryKelpsy.text = "${berry.name} x${qty}"
                        PomegBerry -> chipBerryPomeg.text = "${berry.name} x${qty}"
                        QualotBerry -> chipBerryQualot.text = "${berry.name} x${qty}"
                    }
                }

                player.pokeball.forEach { (ball, qty) ->
                    when (ball) {
                        EmptyBall -> {}
                        GreatBall -> chipBallGreat.text = "${ball.name} x${qty}"
                        LuxuryBall -> chipBallLuxury.text = "${ball.name} x${qty}"
                        MasterBall -> chipBallMaster.text = "${ball.name} x${qty}"
                        NormalBall -> chipBallPoke.text = "${ball.name} x${qty}"
                        UltraBall -> chipBallUltra.text = "${ball.name} x${qty}"
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
            findNavController().popBackStack()
        }

        val berriresChip = listOf(
            chipBerryPomeg to PomegBerry,
            chipBerryKelpsy to KelpsyBerry,
            chipBerryQualot to QualotBerry,
            chipBerryHondew to HondewBerry,
            chipBerryGrepa to GrepaBerry,
        )

        berriresChip.forEach { (chip, berry) ->
            chip.setOnClickListener {
                viewModel.throwBerry(
                    berry = berry,
                    onSuccess = {
                        updateStatus(it)
                    },
                    onFailed = {
                        "You don't have enough berries".snack()
                    }
                )
            }
        }

        val ballsChips = listOf(
            chipBallPoke to NormalBall,
            chipBallLuxury to LuxuryBall,
            chipBallGreat to GreatBall,
            chipBallUltra to UltraBall,
            chipBallMaster to MasterBall,
        )

        ballsChips.forEach { (chip, ball) ->
            chip.setOnClickListener {
                viewModel.throwBall(
                    ball = ball,
                    onSuccess = {
                        catchSuccess(ball)
                    },
                    onFailed = {
                        "you don't have this ball".snack()
                    }
                )
            }
        }

    }

    private fun FragmentPetBinding.catchSuccess(ball: Pokeball) {
        val fillPercent = (progressLove.progress.toFloat() / progressLove.max.toFloat()) * 100
        val isCaptured = (100 - fillPercent) <= ball.successRate
        val msg = if (isCaptured) "Gotcha!!" else "Pokemon Ran away!"
        msg.snack()
        findNavController().popBackStack()
    }

    private fun updateStatus(percent: Int) = binding.run {
        val likeness = randomLikeness(percent)
        val likenessMessage = if (likeness > 0) "Great! loved it..." else "Yukk.. its sour.."
        ("$likenessMessage $likeness").toString().snack()
        progressLove.progress = progressLove.progress + likeness
    }

    fun randomLikeness(amount: Int): Int {
        return when ((1..100).random()) {
            in 70..100 -> -1 * (amount / 2)
            else -> amount
        }
    }

    override fun onDestroyView() {
        countDownTimer?.cancel()
        super.onDestroyView()
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPetBinding
        get() = FragmentPetBinding::inflate
}