package dev.tavarus.boardgamelogger.domain

data class LocalGame(
    override val name: String,
): BaseGame {
    override val objectId: String
        get() = name
    override val displayName: String
        get() = name
}