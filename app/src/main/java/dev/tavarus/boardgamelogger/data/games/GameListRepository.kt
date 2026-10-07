package dev.tavarus.boardgamelogger.data.games

import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.CollectionStatus
import dev.tavarus.boardgamelogger.domain.GameList
import kotlinx.coroutines.flow.Flow

interface GameListRepository {
    fun getGameList(username: String, collectionStatus: CollectionStatus = CollectionStatus.OWN): Flow<RemoteData<GameList>>
}