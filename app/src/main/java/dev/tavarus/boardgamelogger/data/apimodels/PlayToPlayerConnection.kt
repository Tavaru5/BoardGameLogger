package dev.tavarus.boardgamelogger.data.apimodels

import androidx.room.Entity

@Entity(primaryKeys = ["name", "playId"])
data class PlayToPlayerCrossRef(
    val name: String,
    val playId: Int
)

// The Play will have to have a map of player names to scores still
// The reason we need to have a many to many (as opposed to one to many or whatever)
// is so that, in the future, we can get all plays that a player is part of.
