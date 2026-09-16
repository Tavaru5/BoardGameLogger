package dev.tavarus.boardgamelogger.data.plays

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.domain.Play
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaysDao {

    @Insert
    suspend fun insertScore(score: DBScore): Long

    @Insert
    suspend fun insertPlay(play: DBPlay): Long

    @Insert
    suspend fun insertPlayer(player: Player)

    @Transaction
    suspend fun insertWholePlay(play: Play, dbPlay: DBPlay) {
        val playId = insertPlay(dbPlay)
        play.scores.forEach {
            insertScore(DBScore(playerName = it.player.name, playId = playId, score = it.score))
            insertPlayer(it.player)
        }
    }

    @Transaction
    @Query("SELECT * FROM Plays WHERE gameId = :gameId")
    suspend fun getPlays(gameId: String): List<PlayWithScores>

    @Transaction
    @Query("SELECT * FROM Players WHERE name like '%' || :name || '%'")
    suspend fun queryPlayers(name: String): List<Player>
}