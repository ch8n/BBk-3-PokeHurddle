package io.github.ch8n.pokehurddle.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class Player(
    @PrimaryKey val id: String,
    val berries: Map<Berries, Int>,
    val pokeball: Map<Pokeball, Int>,
    val pokemon: List<PokemonDTO>,
    val money: Int
) {
    companion object {
        val Empty = Player(
            id = "",
            berries = emptyMap(),
            pokeball = emptyMap(),
            pokemon = emptyList(),
            money = 0
        )
    }
}

object BerriesMapConverter : TypeMapper<Map<Berries, Int>>()
object PokeballMapConverter : TypeMapper<Map<Pokeball, Int>>()
object PokemonListConverter : TypeMapper<List<PokemonDTO>>()
object PokemonStatsListConverter : TypeMapper<List<Stat>>()


abstract class TypeMapper<T> {

    @TypeConverter
    fun fromString(value: String): T {
        val valueType = object : TypeToken<T>() {}.type
        return Gson().fromJson(value, valueType)
    }

    @TypeConverter
    fun fromStringMap(value: T): String {
        val gson = Gson()
        return gson.toJson(value)
    }
}