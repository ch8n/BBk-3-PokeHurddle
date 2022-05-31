package io.github.ch8n.pokehurddle.data.local.config

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.Pokemon
import io.github.ch8n.pokehurddle.data.models.Stat
import java.lang.reflect.Type

class StringMapConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): Map<String, Int> {
            val mapType: Type = object : TypeToken<Map<String, Int>>() {}.type
            return Gson().fromJson(value, mapType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: Map<String, Int>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}

class PokemonListConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): List<Pokemon> {
            val valueType = object : TypeToken<List<Pokemon>>() {}.type
            return Gson().fromJson(value, valueType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: List<Pokemon>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}
