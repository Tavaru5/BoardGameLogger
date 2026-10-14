package dev.tavarus.boardgamelogger.domain

interface BaseGame {
    val name: String
    val year: String
    val objectId: String
    val displayName: String
        get() = "$name - ($year)"
}