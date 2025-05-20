package dev.tavarus.boardgamelogger.data.apimodels.play

import androidx.room.Embedded
import androidx.room.Relation
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore

data class PlayWithScores(
    @Embedded val play: DBPlay,
    @Relation(
        entity = DBScore::class,
        parentColumn = "playId",
        entityColumn = "playId",
    )
    val scores: List<ScoreWithPlayer>
) {
    fun toDomain(): Play {
        return Play(
            scores = this.scores.map { entry ->
                PlayerScore(entry.player, entry.score.score)
            }
        )
    }
}

data class ScoreWithPlayer(
    @Embedded val score: DBScore,
    @Relation(
        parentColumn = "playerName",
        entityColumn = "name"
    )
    val player: Player,
)
