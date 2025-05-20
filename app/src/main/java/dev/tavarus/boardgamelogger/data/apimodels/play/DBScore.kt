package dev.tavarus.boardgamelogger.data.apimodels.play

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tavarus.boardgamelogger.domain.Score

@Entity(tableName = "scores")
data class DBScore(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Int = 0,
    val playerName: String,
    val playId: Int,
    val score: Score
)
