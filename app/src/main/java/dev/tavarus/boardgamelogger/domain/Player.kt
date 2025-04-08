package dev.tavarus.boardgamelogger.domain

import androidx.compose.ui.graphics.Color

data class Player(
    val name: String,
    val backgroundColor: PlayerColor
)

enum class PlayerColor {
    PURP,
    TEAL,
    PINK,
    YELLOW
}
