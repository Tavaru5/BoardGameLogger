package dev.tavarus.boardgamelogger.data.search

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.data.games.CollectionItem
import dev.tavarus.boardgamelogger.data.games.toDomain

@Xml(name = "items")
data class SearchItems(
    @Attribute(name = "totalitems") val totalItems: Int,
    @Element val itemList: List<SearchItem>?,
)

fun SearchItems.toDomain() = itemList?.filter { it.name.type == "primary" }?.map { it.toDomain() }