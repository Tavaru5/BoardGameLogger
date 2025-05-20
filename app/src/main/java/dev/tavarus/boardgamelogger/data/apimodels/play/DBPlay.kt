package dev.tavarus.boardgamelogger.data.apimodels.play

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plays")
data class DBPlay(
    @PrimaryKey(autoGenerate = true)
    val playId: Int = 0,
    val gameId: String,
)
