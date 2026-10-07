package dev.tavarus.boardgamelogger.data.plays

import dev.tavarus.boardgamelogger.domain.Play
import java.util.Date
import javax.inject.Inject

class PlaysDataRepository @Inject constructor(
    val playsDao: PlaysDao,
) : PlaysRepository {
    override suspend fun getPlays(gameId: String): List<Play> =
        playsDao.getPlays(gameId).map { play ->
                play.toDomain()
        }

    // This should also be an update thing not just an insert (for instance, if the players already exist)
    override suspend fun logPlay(play: Play, gameId: String): Boolean {
        val now = Date().time
        val dbPlay = DBPlay(gameId = gameId, timeStamp = now)
        return playsDao.insertWholePlay(play, dbPlay)
    }

    override suspend fun queryPlayers(name: String): List<Player> =
        playsDao.queryPlayers(name)
}