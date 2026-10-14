package dev.tavarus.boardgamelogger.data.search

import dev.tavarus.boardgamelogger.data.games.GameRemoteDataSource
import dev.tavarus.boardgamelogger.domain.SearchGame
import javax.inject.Inject

class SearchBoardGameUseCaseImpl @Inject constructor(
    val remoteDataSource: GameRemoteDataSource,
): SearchBoardGameUseCase {
    override suspend operator fun invoke(query: String): List<SearchGame> {
        return remoteDataSource.searchGames(query).body()?.toDomain() ?: listOf()
    }
}