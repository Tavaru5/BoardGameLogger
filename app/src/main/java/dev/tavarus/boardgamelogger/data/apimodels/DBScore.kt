package dev.tavarus.boardgamelogger.data.apimodels

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.tavarus.boardgamelogger.domain.Score

// even though this references a playerId, we're not marking it as connected to players because we only ever get it in the scope of
// No wait, do we connect the player to the score instead
// So play <one - many> Score <many - one> player
// Then we might be able to get Plays from players later in a query

// Gotta make the hook up entities between this and play *and* this and player
// Then update the queries


@Entity(tableName = "scores")
data class DBScore(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Int = 0,
    val playerName: String,
    val playId: Int,
    val score: Score
)
