package dev.tavarus.boardgamelogger.domain

data class ListGame(
    val name: String,
    val objectId: String,
    val image: String,
    val thumbnail: String,
    val collectionStatus: CollectionStatus,
)

enum class CollectionStatus {
    OWN,
    PREV_OWNED,
    FOR_TRADE,
    WANT,
    WANT_TO_PLAY,
    WANT_TO_BUY,
    WISHLIST,
    PREORDERED,
    NONE,
}
