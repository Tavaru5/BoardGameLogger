package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.domain.BoardGame
import kotlinx.coroutines.flow.Flow

interface BoardGameRepository {
    fun getBoardGame(id: String): Flow<RemoteData<BoardGame>>
}