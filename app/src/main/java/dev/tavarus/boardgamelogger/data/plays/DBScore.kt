package dev.tavarus.boardgamelogger.data.plays

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tavarus.boardgamelogger.domain.Score

@Entity(tableName = "scores")
data class DBScore(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Long = 0,
    val playerName: String,
    val playId: Long,
    val score: Score
)