package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.DBPlay
import dev.tavarus.boardgamelogger.data.apimodels.toDomain
import dev.tavarus.boardgamelogger.domain.Play
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaysDataRepository @Inject constructor(
    val playsDao: PlaysDao,
) : PlaysRepository {
    override fun getPlays(gameId: String): Flow<List<Play>> =
        playsDao.getPlays(gameId).map { plays ->
            plays.map { play ->
                play.toDomain()
            }
        }

    override fun logPlay(play: Play, gameId: String) {
        playsDao.insertPlay(DBPlay(
            scores = play.scores,
            gameId = gameId,
        ))
    }

}