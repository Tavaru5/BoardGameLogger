package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.domain.CollectionStatus
import javax.inject.Inject

class GameListDataRepository @Inject constructor(
    val gameRemoteDataSource: GameRemoteDataSource
) : GameListRepository {
    override fun getGameList(username: String, collectionStatus: CollectionStatus) = remoteCall {
        val gameList = gameRemoteDataSource.getCollection(username)
        gameList.copy(games = gameList.games.filter { it.collectionStatus == collectionStatus })
    }
}
