package dev.tavarus.boardgamelogger.data.apimodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Players")
data class Player(
    @PrimaryKey val name: String,
    val backgroundColor: PlayerColor
)

enum class PlayerColor {
    PURP,
    TEAL,
    PINK,
    YELLOW
}
