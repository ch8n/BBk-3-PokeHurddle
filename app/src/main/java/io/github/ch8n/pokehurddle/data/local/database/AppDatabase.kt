package io.github.ch8n.pokehurddle.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.github.ch8n.pokehurddle.data.local.config.PokemonListConverter
import io.github.ch8n.pokehurddle.data.local.config.StringMapConverter
import io.github.ch8n.pokehurddle.data.local.sources.PlayerDAO
import io.github.ch8n.pokehurddle.data.local.sources.PokemonDAO
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.Pokemon


@Database(entities = [Player::class, Pokemon::class], version = 1)
@TypeConverters(
    StringMapConverter::class,
    PokemonListConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDAO(): PlayerDAO
    abstract fun pokemonDAO(): PokemonDAO

    companion object {
        fun instance(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "pokeHurddleDB"
            ).build()
        }
    }
}
