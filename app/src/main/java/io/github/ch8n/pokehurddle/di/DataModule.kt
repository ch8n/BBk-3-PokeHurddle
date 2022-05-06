package io.github.ch8n.pokehurddle.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.ch8n.pokehurddle.data.local.database.AppDatabase
import io.github.ch8n.pokehurddle.data.local.sources.PlayerDAO
import io.github.ch8n.pokehurddle.data.local.sources.PokemonDAO
import io.github.ch8n.pokehurddle.data.remote.PokemonService
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideOkhttpClient() = OkHttpClient()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.instance(appContext)

    @Singleton
    @Provides
    fun providePlayerDAO(appDatabase: AppDatabase) = appDatabase.playerDAO()

    @Singleton
    @Provides
    fun providePokemonDAO(appDatabase: AppDatabase) = appDatabase.pokemonDAO()

    @Singleton
    @Provides
    fun providePokemonService(okHttpClient: OkHttpClient) = PokemonService(okHttpClient)

    @Singleton
    @Provides
    fun provideAppRepository(
        pokemonService: PokemonService,
        pokemonDAO: PokemonDAO,
        playerDAO: PlayerDAO
    ) = AppRepository(pokemonService, pokemonDAO, playerDAO, Dispatchers.IO)

}