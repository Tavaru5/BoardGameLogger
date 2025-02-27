package dev.tavarus.boardgamelogger.data.apimodels

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import dev.tavarus.boardgamelogger.domain.BoardGame

@Xml(name = "item")
data class BoardGameItem(
    @Attribute val type: String,
    @Attribute val id: Int,
    @PropertyElement val image: String,
    @PropertyElement val thumbnail: String,
    @PropertyElement val description: String,
    @Element val nameList: List<NameAlternative>,
    @Element val polls: List<Poll>,
    @Element val links: List<Link>,
    @Path("yearpublished") @Attribute(name = "value") val yearPublished: String,
    @Path("minplayers") @Attribute(name = "value") val minPlayers: String,
    @Path("maxplayers") @Attribute(name = "value") val maxPlayers: String,
    @Path("playingtime") @Attribute(name = "value") val playingTime: Int,
    @Path("minplayingtime") @Attribute(name = "value") val minPlayingTime: Int,
    @Path("maxplayingtime") @Attribute(name = "value") val maxPlayingTime: Int,
)

@Xml(name = "name")
data class NameAlternative(
    @Attribute val type: String,
    @Attribute val value: String,
)

@Xml(name = "poll")
data class Poll(
    @Attribute val name: String,
    @Attribute val title: String,
    @Attribute val totalvotes: Int,
    @Element val results: List<PollResults>
)

@Xml(name = "results")
data class PollResults(
    @Attribute val numplayers: String? = null,
    @Element val results: List<PollResult>
)

@Xml(name = "result")
data class PollResult(
    @Attribute val value: String,
    @Attribute val numVotes: Int,
)

@Xml(name = "link")
data class Link(
    @Attribute val type: String,
    @Attribute val id: String,
    @Attribute val value: String,
)

fun BoardGameItem.toDomain(): BoardGame {
    val name = nameList.first { it.type == "primary" }
    return BoardGame(
        name = name.value,
        objectId = id.toString(),
        image = image,
        thumbnail = thumbnail,
    )
}

