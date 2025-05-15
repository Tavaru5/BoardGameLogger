package dev.tavarus.boardgamelogger.data.apimodels

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import dev.tavarus.boardgamelogger.domain.Play
import dev.tavarus.boardgamelogger.domain.PlayerScore
import dev.tavarus.boardgamelogger.domain.Score

@Entity(tableName = "Plays")
data class DBPlay(
    @PrimaryKey(autoGenerate = true)
    val playId: Int = 0,
    val gameId: String,
)

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


class ScoreConverter() {
    @TypeConverter
    fun fromDBString(value: String?): Score? = Score.fromDBString(value)

    @TypeConverter
    fun scoreToDBString(score: Score?): String? = score?.toDBString()

}

