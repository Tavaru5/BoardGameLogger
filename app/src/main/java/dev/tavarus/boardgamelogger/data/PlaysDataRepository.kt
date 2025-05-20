package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.play.DBPlay
import dev.tavarus.boardgamelogger.data.apimodels.play.DBScore
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

    // This should also be an update thing not just an insert (for instance, if the players already exist)
    override fun logPlay(play: Play, gameId: String) {
        val dbPlay = DBPlay(gameId = gameId)
        val scores = play.scores.map {
            DBScore(playerName = it.player.name, playId = dbPlay.playId, score = it.score)
        }
        val players = play.scores.map { it.player }
        playsDao.insertPlay(
            DBPlay(
                gameId = gameId,
            ),
            scores = scores,
            players,
        )
    }

}