package dev.tavarus.boardgamelogger

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.tavarus.boardgamelogger.data.BoardGameRepository
import dev.tavarus.boardgamelogger.data.GameListRepository
import dev.tavarus.boardgamelogger.data.NetworkModule
import dev.tavarus.boardgamelogger.data.RemoteData
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.CollectionStatus
import dev.tavarus.boardgamelogger.domain.GameList
import dev.tavarus.boardgamelogger.domain.ListType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
object FakeNetworkModule {
    @Singleton
    @Provides
    fun provideFakeGameListRepository() = object : GameListRepository {
        override fun getGameList(
            username: String,
            collectionStatus: CollectionStatus,
        ): Flow<RemoteData<GameList>> {
            return flowOf(
                RemoteData.Success(
                    GameList(
                        ListType.PersonalCollection("tav"),
                        listOf()
                    )
                )
            )
        }
    }

    @Singleton
    @Provides
    fun providesFakeBoardGameRepository() = object : BoardGameRepository {

        override fun getBoardGame(id: String): Flow<RemoteData<BoardGame>> {
            return flowOf(
                RemoteData.Loading //TODO
            )
        }
    }
}