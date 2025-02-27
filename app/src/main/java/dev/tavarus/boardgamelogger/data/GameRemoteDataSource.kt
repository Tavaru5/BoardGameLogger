package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.toDomain
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.GameList
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(
    val apiService: BGGApiService
) {
    suspend fun getCollection(username: String): GameList = apiService
        .getCollection(username)
        .toDomain(username)

    suspend fun getGame(id: String): BoardGame = apiService
        .getThing(id)
        .toDomain()
}






