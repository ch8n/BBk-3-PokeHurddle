package io.github.ch8n.pokehurddle.data.local.config

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.ch8n.pokehurddle.data.models.Berries
import io.github.ch8n.pokehurddle.data.models.Pokeball
import io.github.ch8n.pokehurddle.data.models.PokemonDTO
import io.github.ch8n.pokehurddle.data.models.Stat

class BerriesMapConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): HashMap<Berries, Int> {
            val map = Gson().fromJson(value, Any::class.java)
            return HashMap(map as Map<Berries, Int>)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: HashMap<Berries, Int>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}

class PokeballMapConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): HashMap<Pokeball, Int> {
            println(value)
            val map = Gson().fromJson(value, Any::class.java)
            return HashMap(map as Map<Pokeball, Int>)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: HashMap<Pokeball, Int>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}

class PokemonListConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): List<PokemonDTO> {
            val valueType = object : TypeToken<List<PokemonDTO>>() {}.type
            return Gson().fromJson(value, valueType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: List<PokemonDTO>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}

class PokemonStatsListConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): List<Stat> {
            val valueType = object : TypeToken<List<Stat>>() {}.type
            return Gson().fromJson(value, valueType)
        }

        @TypeConverter
        @JvmStatic
        fun fromStringMap(value: List<Stat>): String {
            val gson = Gson()
            return gson.toJson(value)
        }
    }

}

