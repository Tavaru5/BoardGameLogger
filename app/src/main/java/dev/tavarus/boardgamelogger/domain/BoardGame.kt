package dev.tavarus.boardgamelogger.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "boardgames")
data class BoardGame(
    override val name: String,
    @PrimaryKey override val objectId: String,
    val image: String,
    val thumbnail: String,
    val year: String,
    val description: String,
    val playerCount: PlayerCount,
    val playTime: PlayTime,
) : BaseGame {
    override val displayName: String
        get() = "$name - ($year)"
}

data class PlayerCount(
    val min: Int,
    val max: Int,
)

data class PlayTime(
    val min: Int,
    val average: Int,
    val max: Int,
)

