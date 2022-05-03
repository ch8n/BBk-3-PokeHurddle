package io.github.ch8n.pokehurddle.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.ch8n.pokehurddle.data.local.sources.PlayerDAO
import io.github.ch8n.pokehurddle.data.local.sources.PokemonDAO
import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.models.PokemonDTO


@Database(entities = [Player::class, PokemonDTO::class], version = 1)
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
