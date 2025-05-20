package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.domain.Play
import kotlinx.coroutines.flow.Flow

interface PlaysRepository {
    fun getPlays(gameId: String): Flow<List<Play>>
    fun logPlay(play: Play, gameId: String)
}