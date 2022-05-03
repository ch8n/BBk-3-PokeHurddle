package io.github.ch8n.pokehurddle.ui.catchPokemon

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainViewModel
import io.github.ch8n.setVisible
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class CatchPokemonFragment : Fragment() {
    private var binding: FragmentPetBinding? = null

    private var countDownTimer: CountDownTimer? = null
    private val viewModel: MainViewModel by activityViewModels()

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
        setup()
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

    private fun setup() = with(requireNotNull(binding)) {

        val pokemonInBattle = viewModel.pokemonEncountered.value ?: return

        lifecycleScope.launchWhenResumed {
            viewModel.playerStats.collect {
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

                player.pokeball.forEach { (ball, qty) ->
                    when (ball) {
                        Pokeball.Empty -> {}
                        Pokeball.GreatBall -> chipBallGreat.text = "${ball.name} x${qty}"
                        Pokeball.LuxuryBall -> chipBallLuxury.text = "${ball.name} x${qty}"
                        Pokeball.MasterBall -> chipBallMaster.text = "${ball.name} x${qty}"
                        Pokeball.PokeBall -> chipBallPoke.text = "${ball.name} x${qty}"
                        Pokeball.UltraBall -> chipBallUltra.text = "${ball.name} x${qty}"
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
            chipBerryPomeg to Berries.PomegBerry,
            chipBerryKelpsy to Berries.KelpsyBerry,
            chipBerryQualot to Berries.QualotBerry,
            chipBerryHondew to Berries.HondewBerry,
            chipBerryGrepa to Berries.GrepaBerry,
        )

        berriresChip.forEach { (chip, berry) ->
            chip.setOnClickListener {
                viewModel.throwBerry(
                    berry = berry,
                    onSuccess = {
                        updateStatus(it)
                    },
                    onFailed = {
                        "You don't have enough berries".toast()
                    }
                )
            }
        }

        val ballsChips = listOf(
            chipBallPoke to Pokeball.PokeBall,
            chipBallLuxury to Pokeball.LuxuryBall,
            chipBallGreat to Pokeball.GreatBall,
            chipBallUltra to Pokeball.UltraBall,
            chipBallMaster to Pokeball.MasterBall,
        )

        ballsChips.forEach { (chip, ball) ->
            chip.setOnClickListener {
                viewModel.throwBall(
                    ball = ball,
                    onSuccess = {
                        catchSuccess(ball)
                    },
                    onFailed = {
                        "you don't have this ball".toast()
                    }
                )
            }
        }

    }

    private fun FragmentPetBinding.catchSuccess(ball: Pokeball) {
        val fillPercent = (progressLove.progress.toFloat() / progressLove.max.toFloat()) * 100
        val isCaptured = (100 - fillPercent) <= ball.successRate
        val msg = if (isCaptured) "Gotcha!!" else "Pokemon Ran away!"
        msg.toast()
        findNavController().popBackStack()
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