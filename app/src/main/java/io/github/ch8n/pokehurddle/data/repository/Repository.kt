package io.github.ch8n.pokehurddle.data.repository

import android.util.Log
import com.google.gson.Gson
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Encounter
import io.github.ch8n.pokehurddle.data.models.Pokeballs
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import okhttp3.OkHttpClient
import okhttp3.Request

class AppRepository {

    val randomPokeBall: Pokeballs
        get() {
            val randomIndex = (0..100).random()
            return when (randomIndex) {
                in 0..10 -> Pokeballs.MasterBall
                in 10..25 -> Pokeballs.UltraBall
                in 25..55 -> Pokeballs.GreatBall
                in 55..75 -> Pokeballs.LuxuryBall
                else -> Pokeballs.PokeBall
            }
        }

    private val pokemonCache = mutableMapOf<Int, PokemonDTO>()
    val randomPokemon: PokemonDTO?
        get() {
            val client = OkHttpClient()
            val randomPokemonIndex = (0..1126).random()
            return pokemonCache.getOrElse(randomPokemonIndex) {
                val pokemonRequest = Request.Builder()
                    .url("https://pokeapi.co/api/v2/pokemon/$randomPokemonIndex")
                    .build()
                val response = kotlin.runCatching {
                    val data = client.newCall(pokemonRequest).execute()
                    val jsonString = data.body?.string()
                    Gson().fromJson(jsonString, PokemonDTO::class.java)
                }
                val pokemonDTO = response.getOrElse {
                    Log.e("Error", "Pokemon fetch failed", it)
                    null
                }
                pokemonDTO?.let {
                    pokemonCache.put(randomPokemonIndex, it)
                }
                pokemonDTO
            }
        }

    val randomBerry: Berries
        get() {
            val randomIndex = (0..100).random()
            return when (randomIndex) {
                in 0..10 -> Berries.GrepaBerry
                in 10..25 -> Berries.HondewBerry
                in 25..55 -> Berries.QualotBerry
                in 55..75 -> Berries.KelpsyBerry
                else -> Berries.PomegBerry
            }
        }

    val randomEncounter: Encounter
        get() {
            val random = (0..100).random()
            return when (random) {
                in 0..20 -> Encounter.Pokemon
                in 20..30 -> Encounter.PokeBall
                in 30..50 -> Encounter.Berry
                in 50..60 -> Encounter.Money
                else -> Encounter.Nothing
            }
        }

}