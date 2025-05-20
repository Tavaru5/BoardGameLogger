package dev.tavarus.boardgamelogger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.tavarus.boardgamelogger.data.apimodels.play.DBPlay
import dev.tavarus.boardgamelogger.data.apimodels.play.DBScore
import dev.tavarus.boardgamelogger.data.apimodels.Player
import dev.tavarus.boardgamelogger.data.apimodels.play.ScoreConverter

@Database(entities = [DBPlay::class, DBScore::class, Player::class], version = 1)
@TypeConverters(ScoreConverter::class)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun playsDao(): PlaysDao
}