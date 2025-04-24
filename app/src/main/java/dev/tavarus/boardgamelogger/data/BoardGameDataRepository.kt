package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.toDomain
import dev.tavarus.boardgamelogger.domain.BoardGame
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BoardGameDataRepository @Inject constructor(
    val gameRemoteDataSource: GameRemoteDataSource
): BoardGameRepository {
    override fun getBoardGame(id: String): Flow<RemoteData<BoardGame>> {
        return remoteCall {
            gameRemoteDataSource.getGame(id).toDomain()
        }
    }
}