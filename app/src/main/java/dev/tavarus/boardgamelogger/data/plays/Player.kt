package dev.tavarus.boardgamelogger.data.plays

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Players")
data class Player(
    @PrimaryKey val name: String,
    val backgroundColor: PlayerColor = PlayerColor.PURP // idk if this needs to be tied to a player
)

enum class PlayerColor {
    PURP,
    TEAL,
    PINK,
    YELLOW
}
