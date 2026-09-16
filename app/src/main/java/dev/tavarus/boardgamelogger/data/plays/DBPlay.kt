package dev.tavarus.boardgamelogger.data.plays

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plays")
data class DBPlay(
    @PrimaryKey(autoGenerate = true)
    val playId: Long = 0,
    val gameId: String,
    val timeStamp: Long,
)