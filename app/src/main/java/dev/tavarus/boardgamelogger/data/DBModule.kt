package dev.tavarus.boardgamelogger.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {
    @Provides
    @Singleton
    fun provideGamesDatabase(
        @ApplicationContext context: Context
    ) : GamesDatabase = Room.databaseBuilder(
        context,
        GamesDatabase::class.java, "games-database"
    ).build()

    @Provides
    @Singleton
    fun providePlaysDao(db: GamesDatabase) = db.playsDao()

    @Provides
    fun providePlaysRepository(dao: PlaysDao) = PlaysDataRepository(dao)
}