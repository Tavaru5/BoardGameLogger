package dev.tavarus.boardgamelogger.data.search

import dev.tavarus.boardgamelogger.domain.SearchGame

interface SearchBoardGameUseCase {
    suspend operator fun invoke(query: String): List<SearchGame>
}