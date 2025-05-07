package dev.tavarus.boardgamelogger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.tavarus.boardgamelogger.data.apimodels.DBPlay

@Database(entities = [DBPlay::class], version = 1)
abstract class GamesDatabase : RoomDatabase() {
    abstract fun playsDao(): PlaysDao
}