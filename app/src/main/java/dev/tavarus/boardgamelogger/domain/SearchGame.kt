package dev.tavarus.boardgamelogger.domain

data class SearchGame(
    override val name: String,
    override val objectId: String,
    val year: String,
) : BaseGame {
    override val displayName: String
        get() = "$name - ($year)"
}
