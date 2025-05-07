package dev.tavarus.boardgamelogger.data.apimodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore

@Entity(tableName = "Plays")
data class DBPlay(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val scores: List<PlayerScore>,
    val gameId: String,
)

fun DBPlay.toDomain() = Play(
    scores = scores
)
