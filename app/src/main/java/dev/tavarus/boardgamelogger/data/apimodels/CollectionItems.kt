package dev.tavarus.boardgamelogger.data.apimodels

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.GameList
import dev.tavarus.boardgamelogger.domain.ListType

@Xml(name = "items")
data class CollectionItems(
    @Attribute(name = "totalitems") val totalItems: Int,
    @Element val itemList: List<CollectionItem>,
)


fun CollectionItems.toDomain(username: String) = GameList(
    type = ListType.PersonalCollection(username),
    games = itemList.map { it.toDomain() }
)
