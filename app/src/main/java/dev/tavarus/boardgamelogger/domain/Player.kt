package dev.tavarus.boardgamelogger.domain


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
