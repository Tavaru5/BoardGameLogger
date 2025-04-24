package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.BoardGameItems
import dev.tavarus.boardgamelogger.data.apimodels.CollectionItems
import dev.tavarus.boardgamelogger.data.apimodels.toDomain
import dev.tavarus.boardgamelogger.domain.BoardGame
import dev.tavarus.boardgamelogger.domain.GameList
import retrofit2.Response
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(
    val apiService: BGGApiService
) {
    suspend fun getCollection(username: String): Response<CollectionItems> = apiService
        .getCollection(username)

    suspend fun getGame(id: String): BoardGameItems = apiService
        .getThing(id)
}






