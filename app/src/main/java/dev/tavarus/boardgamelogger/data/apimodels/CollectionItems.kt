package dev.tavarus.boardgamelogger.data.apimodels

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.GameList
import dev.tavarus.boardgamelogger.domain.ListType
import retrofit2.Response

@Xml(name = "items")
data class CollectionItems(
    @Attribute(name = "totalitems") val totalItems: Int,
    @Element val itemList: List<CollectionItem>?,
)


fun Response<CollectionItems>.toDomain(username: String): GameList {
    if (this.code() == 202) {
        throw(Error("Need to wait for BGG full response"))
    } else {
        return GameList(
            type = ListType.PersonalCollection(username),
            games = this.body()?.itemList?.map { it.toDomain() } ?: listOf()
        )
    }

}
