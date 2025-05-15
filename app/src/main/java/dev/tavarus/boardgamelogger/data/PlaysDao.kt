package dev.tavarus.boardgamelogger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.tavarus.boardgamelogger.data.apimodels.DBPlay
import dev.tavarus.boardgamelogger.data.apimodels.DBScore
import dev.tavarus.boardgamelogger.data.apimodels.PlayWithScores
import dev.tavarus.boardgamelogger.data.apimodels.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaysDao {
    @Transaction
    @Insert
    fun insertPlay(play: DBPlay, scores: List<DBScore>, players: List<Player>)

    @Transaction
    @Query("SELECT * FROM Plays WHERE gameId = :gameId")
    fun getPlays(gameId: String): Flow<List<PlayWithScores>>
}