package dev.tavarus.boardgamelogger.data

import androidx.room.Dao
import androidx.room.Query
import dev.tavarus.boardgamelogger.domain.BoardGame

@Dao
interface BoardGameDao {
    @Query("SELECT * FROM boardgames")
    fun getAll(): List<BoardGame>
}