package io.github.ch8n.pokehurddle.data.usecases

import io.github.ch8n.pokehurddle.data.models.Player
import io.github.ch8n.pokehurddle.data.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObservablePlayerStats @Inject constructor(
    private val appRepository: AppRepository
) {
    operator fun invoke(): Flow<Player> = appRepository.getPlayer()
}