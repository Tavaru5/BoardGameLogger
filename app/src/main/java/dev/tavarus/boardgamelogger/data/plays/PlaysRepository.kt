package dev.tavarus.boardgamelogger.data.plays

import dev.tavarus.boardgamelogger.domain.Play

interface PlaysRepository {
    suspend fun getPlays(gameId: String): List<Play>
    suspend fun logPlay(play: Play, gameId: String): Boolean
    suspend fun queryPlayers(name: String): List<Player>
}