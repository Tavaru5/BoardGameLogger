package dev.tavarus.boardgamelogger.data.apimodels

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.CollectionStatus
import dev.tavarus.boardgamelogger.domain.ListGame

@Xml(name = "item")
data class CollectionItem(
    @Attribute(name = "objecttype") val objectType: String,
    @Attribute(name = "objectid") val objectId: Int,
    @Attribute val subtype: String,
    @Attribute(name = "collid") val collId: Int,
    @PropertyElement(name = "yearpublished") val yearPublished: Int,
    @PropertyElement val image: String,
    @PropertyElement val thumbnail: String,
    @PropertyElement val name: String,
    @Element val status: Status,
)

@Xml
data class Status(
    @Attribute val own: Int,
    @Attribute val prevowned: Int,
    @Attribute val fortrade: Int,
    @Attribute val want: Int,
    @Attribute val wanttoplay: Int,
    @Attribute val wanttobuy: Int,
    @Attribute val wishlist: Int,
    @Attribute val preordered: Int,
)

fun CollectionItem.toDomain() = ListGame(
    name = name,
    objectId = objectId.toString(),
    image = image,
    thumbnail = thumbnail,
    collectionStatus = status.toDomain(),
)

fun Status.toDomain(): CollectionStatus =
    if (this.own > 0) {
        CollectionStatus.OWN
    } else if (this.want > 0) {
        CollectionStatus.WANT
    } else if (this.fortrade > 0) {
        CollectionStatus.FOR_TRADE
    } else if (this.wishlist > 0) {
        CollectionStatus.WISHLIST
    } else if (this.prevowned > 0) {
        CollectionStatus.PREV_OWNED
    } else if (this.preordered > 0) {
        CollectionStatus.PREORDERED
    } else if (this.wanttobuy > 0) {
        CollectionStatus.WANT_TO_BUY
    } else if (this.wanttoplay > 0) {
        CollectionStatus.WANT_TO_PLAY
    } else {
        CollectionStatus.NONE
    }
