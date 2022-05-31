package io.github.ch8n.pokehurddle.data.remote

import android.util.Log
import com.google.gson.Gson
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

// one instance in dependency graph
@Singleton
class PokemonService @Inject constructor(
    //👇 okhttp client is used for doing api calls
    private val okHttpClient: OkHttpClient
) {

    // fetch pokemon with ID
    suspend fun fetchPokemon(id: Int): PokemonDTO? = withContext(Dispatchers.IO) {
        // this scope runs over IO dispatcher not on main dispatcher.

        // 👇 api url where we will fetch pokemon details from
        val endpoint = "https://pokeapi.co/api/v2/pokemon/$id"

        //👇 okHttp request builder for making api call
        val pokemonRequest = Request.Builder()
            .url(endpoint)
            .build()

        val response = kotlin.runCatching {
            //👇 use okhttp client to do new request
            val data = okHttpClient.newCall(pokemonRequest).execute()
            //👇 get response body as string
            val jsonString = data.body?.string()
            //👇 convert jsonString to Pokemon instance
            Gson().fromJson(jsonString, PokemonDTO::class.java)
        }

        // 👇 we will get pokemon instance if api call went well or..
        val pokemonDTO = response.getOrElse {
            // 👇 ...for any error this section is executed
            Log.e("Error", "Pokemon fetch failed", it)
            null
        }
        // 👇 we return nullable response from this function
        pokemonDTO
    }
}
