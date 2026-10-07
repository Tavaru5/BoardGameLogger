package dev.tavarus.boardgamelogger.data.plays

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.domain.Play
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaysDao {

    @Insert
    suspend fun insertScore(score: DBScore): Long

    @Insert
    suspend fun insertPlay(play: DBPlay): Long

    @Upsert
    suspend fun upsertPlayer(player: Player)

    @Transaction
    suspend fun insertWholePlay(play: Play, dbPlay: DBPlay): Boolean {
        var success = true
        val playId = insertPlay(dbPlay)
        play.scores.forEach {
            if (insertScore(DBScore(playerName = it.player.name, playId = playId, score = it.score)) == -1L) {
                success = false
            }
            upsertPlayer(it.player)
        }
        return success
    }

    @Transaction
    @Query("SELECT * FROM Plays WHERE gameId = :gameId")
    suspend fun getPlays(gameId: String): List<PlayWithScores>

    @Transaction
    @Query("SELECT * FROM Players WHERE name like '%' || :name || '%'")
    suspend fun queryPlayers(name: String): List<Player>
}