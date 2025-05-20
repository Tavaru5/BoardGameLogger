package dev.tavarus.boardgamelogger.data.apimodels.play

import androidx.room.TypeConverter
import dev.tavarus.boardgamelogger.domain.Score

class ScoreConverter {
    @TypeConverter
    fun fromDBString(value: String?): Score? = Score.fromDBString(value)

    @TypeConverter
    fun scoreToDBString(score: Score?): String? = score?.toDBString()

}
