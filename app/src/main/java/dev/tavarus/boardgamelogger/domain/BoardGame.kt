package dev.tavarus.boardgamelogger.domain

import kotlinx.serialization.Serializable

@Serializable
data class BoardGame(
    val name: String,
    val objectId: String,
    val image: String,
    val thumbnail: String,
    // There will probably be more things to put in here;
    // This is a snapshot for the list page, probably doesn't even need image just thumbnail?
    // We might want a local "numPlays" as well; that wouldn't be the one from bgg's api, but rather from our local source
)

