package io.github.ch8n.data.repository

import android.util.Log
import com.google.gson.Gson
import io.github.ch8n.data.models.*
import okhttp3.OkHttpClient
import okhttp3.Request

class AppRepository {

    val randomPokeBall: Pokeball
        get() {
            val randomIndex = (0..100).random()
            return when (randomIndex) {
                in 0..10 -> pokeBalls.last()
                in 10..25 -> pokeBalls.get(3)
                in 25..55 -> pokeBalls.get(2)
                in 55..75 -> pokeBalls.get(1)
                else -> pokeBalls.first()
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

    val randomBerry: Berry
        get() {
            val randomIndex = (0..100).random()
            return when (randomIndex) {
                in 0..10 -> berries.last()
                in 10..25 -> berries.get(3)
                in 25..55 -> berries.get(2)
                in 55..75 -> berries.get(1)
                else -> berries.first()
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