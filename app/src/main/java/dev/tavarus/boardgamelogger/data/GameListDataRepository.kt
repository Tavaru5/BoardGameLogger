package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.toDomain
import dev.tavarus.boardgamelogger.domain.CollectionStatus
import javax.inject.Inject

class GameListDataRepository @Inject constructor(
    val gameRemoteDataSource: GameRemoteDataSource
) : GameListRepository {
    override fun getGameList(username: String, collectionStatus: CollectionStatus) = remoteCall {
        var gameList = gameRemoteDataSource.getCollection(username).toDomain(username)
        gameList.copy(games = gameList.games.filter { it.collectionStatus == collectionStatus })
    }
}
