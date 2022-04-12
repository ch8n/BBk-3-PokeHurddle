package io.github.ch8n.pokehurddle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import io.github.ch8n.pokehurddle.databinding.ActivityMainBinding

sealed class Action {
    object Idle : Action()
    object Pokemon : Action()
    object Item : Action()
    object Berry : Action()
}

/***
 *  home screen
 *      --- actions
 *          -- explore ---> encounter
 *          -- bag -- berries | pokeball
 *          -- pokemon -- entire collection
 *          -- poke-center -- recover fatigue --> money cost == pokemon same as attack points
 *
 *  Encounter
 *      -- Berry
 *      -- Pokeball
 *      -- Nothing
 *      -- Pokemon
 *      -- Money
 *  Pokemon
 *      -- Pet --> berry* & Pokeball*
 *      -- Fight --> Pokeball* & pokemon*
 *      -- Escape
 *
 *  Pokemon fight
 *      -- Select pokemon
 *      -- tap under 10 second ==> damage == pokemon attack
 *      -- after 10 seconds ===> select pokeball ===> capture == successRate
 *      -- player pokemon --> fatigue points 10 ==> -1
 *
 *  Pokemon Pet
 *      -- select berry
 *      -- tap under 10 second ==> damage == berry attract rate
 *      -- if health == 0 --> capture pokemon
 ***/


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() = with(binding) {
        findNavController(R.id.container_nav_host)
    }


}