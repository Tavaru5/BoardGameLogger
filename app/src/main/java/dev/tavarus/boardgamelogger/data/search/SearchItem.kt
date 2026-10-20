package dev.tavarus.boardgamelogger.data.search

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.SearchGame

@Xml(name = "item")
data class SearchItem(
    @Attribute(name = "type") val type: String,
    @Attribute(name = "id") val id: Int,
    @Element(name = "name") val name: SearchName,
    @Element(name = "yearpublished") val yearPublished: YearPublished,
)

@Xml
data class SearchName(
    @Attribute(name = "type") val type: String,
    @Attribute(name = "value") val value: String,
)

@Xml
data class YearPublished(
    @Attribute(name = "value") val value: Int
)

fun SearchItem.toDomain() = SearchGame(
    name = name.value,
    year = yearPublished.value.toString(),
    objectId = id.toString()
)