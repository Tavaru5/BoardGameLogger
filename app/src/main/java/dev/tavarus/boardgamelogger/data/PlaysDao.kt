package dev.tavarus.boardgamelogger.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.tavarus.boardgamelogger.data.apimodels.DBPlay
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaysDao {
    @Insert
    fun insertPlay(play: DBPlay)

    @Query("SELECT * FROM Plays WHERE gameId = :gameId")
    fun getPlays(gameId: String): Flow<List<DBPlay>>
}