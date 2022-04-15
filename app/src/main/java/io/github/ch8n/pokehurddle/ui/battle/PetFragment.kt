package io.github.ch8n.pokehurddle.ui.battle

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.databinding.FragmentPetBinding
import io.github.ch8n.pokehurddle.ui.MainActivity


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
        // TODO --> player invertory observing
        // if out of berry end session
        // select pokeball on session end
        // calculate catch rate
        // if sucess add to db
        // else pokemon escape
        val pokemonInBattle = viewModel.pokemonEncounter ?: return

        startPetTimer(
            onSessionOver = {
                Toast.makeText(requireContext(), "Session over", Toast.LENGTH_SHORT).show()
                //TODO continiue
//                containerBerries.setVisible(false)
//                containerPokeball.setVisible(true)
            }
        )

        Glide.with(requireContext())
            .load(pokemonInBattle.sprites.front_default)
            .into(imgPokemon)

        labelPokemon.text = pokemonInBattle.name
        progressLove.max = pokemonInBattle.health

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.setEscapedFromBattleOrPet(true)
            findNavController().popBackStack()
        }

        chipBerryPomeg.setOnClickListener {
            val pomeg = Berries.PomegBerry
            val percent = pokemonInBattle.health / pomeg.attractionRate
            percent.toString().toast()
            progressLove.progress = progressLove.progress + percent
        }

        chipBerryKelpsy.setOnClickListener {
            val kelpsy = Berries.KelpsyBerry
            val percent = pokemonInBattle.health / kelpsy.attractionRate
            percent.toString().toast()
            progressLove.progress = progressLove.progress + percent
        }

        chipBerryQualot.setOnClickListener {
            val qualot = Berries.QualotBerry
            val percent = pokemonInBattle.health / qualot.attractionRate
            percent.toString().toast()
            progressLove.progress = progressLove.progress + percent
        }

        chipBerryHondew.setOnClickListener {
            val hondew = Berries.HondewBerry
            val percent = pokemonInBattle.health / hondew.attractionRate
            percent.toString().toast()
            progressLove.progress = progressLove.progress + percent
        }

        chipBerryGrepa.setOnClickListener {
            val grepa = Berries.GrepaBerry
            val percent = pokemonInBattle.health / grepa.attractionRate
            percent.toString().toast()
            progressLove.progress = progressLove.progress + percent
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