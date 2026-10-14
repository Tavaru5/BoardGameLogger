package dev.tavarus.boardgamelogger.domain

data class SearchGame(
    override val name: String,
    override val objectId: String,
    override val year: String,
) : BaseGame
