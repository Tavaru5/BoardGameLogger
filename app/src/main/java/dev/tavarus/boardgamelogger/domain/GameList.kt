package dev.tavarus.boardgamelogger.domain

data class GameList(
    val type: ListType,
    val games: List<ListGame>
)

sealed class ListType {
    data object PreviouslyPlayed: ListType()
    data class PersonalCollection(val username: String): ListType()
}
/* idk if we want these to be a Room Entity
 * If they are an entity: They are just lists of ids to board games.
 * If they aren't an entity, we could just add a field of "type" or something to the board game for the different lists that they're part of
 * Which would mean we would just query boardgames by type
 * idk if that's more performant or not?
 * We would have to add a field to the board games for when we store them which makes me think it's probably worse since it no longer mirrors
 *
*/

