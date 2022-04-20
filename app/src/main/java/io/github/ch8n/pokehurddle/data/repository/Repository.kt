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
                in 25..45 -> Pokeballs.GreatBall
                in 45..55 -> Pokeballs.LuxuryBall
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
                in 0..5 -> Berries.GrepaBerry
                in 5..15 -> Berries.HondewBerry
                in 15..30 -> Berries.QualotBerry
                in 30..55 -> Berries.KelpsyBerry
                else -> Berries.PomegBerry
            }
        }

    val randomEncounter: Encounter
        get() {
            val random = (0..100).random()
            return when (random) {
                in 0..10 -> Encounter.Pokemon
                in 10..20 -> Encounter.PokeBall
                in 20..30 -> Encounter.Berry
                in 30..50 -> Encounter.Money
                else -> Encounter.Nothing
            }
        }
}