package dev.tavarus.boardgamelogger.data.games

import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.BoardGame
import kotlinx.coroutines.flow.Flow

interface BoardGameRepository {
    fun getBoardGame(id: String): Flow<RemoteData<BoardGame>>
}