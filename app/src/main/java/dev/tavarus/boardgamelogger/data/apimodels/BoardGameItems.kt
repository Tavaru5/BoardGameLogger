package dev.tavarus.boardgamelogger.data.apimodels

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.BoardGame

@Xml(name = "items")
data class BoardGameItems(
    @Element val item: BoardGameItem
)

fun BoardGameItems.toDomain(): BoardGame = item.toDomain()