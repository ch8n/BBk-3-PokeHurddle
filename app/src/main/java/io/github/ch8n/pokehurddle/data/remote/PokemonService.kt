package io.github.ch8n.pokehurddle.data.remote

import android.util.Log
import com.google.gson.Gson
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {

    fun fetchPokemon(id: Int): PokemonDTO? {
        val pokemonRequest = Request.Builder()
            .url("https://pokeapi.co/api/v2/pokemon/$id")
            .build()
        val response = kotlin.runCatching {
            val data = okHttpClient.newCall(pokemonRequest).execute()
            val jsonString = data.body?.string()
            Gson().fromJson(jsonString, PokemonDTO::class.java)
        }
        val pokemonDTO = response.getOrElse {
            Log.e("Error", "Pokemon fetch failed", it)
            null
        }
        return pokemonDTO
    }
}
