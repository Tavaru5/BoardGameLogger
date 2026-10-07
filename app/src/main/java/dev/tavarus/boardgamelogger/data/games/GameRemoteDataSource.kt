package dev.tavarus.boardgamelogger.data.games

import dev.tavarus.boardgamelogger.data.BGGApiService
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