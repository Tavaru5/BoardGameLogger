package dev.tavarus.boardgamelogger.domain

data class GameList(
    val type: ListType,
    val games: List<ListGame>
)

sealed class ListType {
    data object PreviouslyPlayed: ListType()
    data class PersonalCollection(val username: String): ListType()
}

