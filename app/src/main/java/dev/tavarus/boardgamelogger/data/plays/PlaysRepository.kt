package dev.tavarus.boardgamelogger.data.plays

import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.domain.Play
import kotlinx.coroutines.flow.Flow

interface PlaysRepository {
    suspend fun getPlays(gameId: String): List<Play>
    suspend fun logPlay(play: Play, gameId: String): Boolean
    suspend fun queryPlayers(name: String): List<Player>
}