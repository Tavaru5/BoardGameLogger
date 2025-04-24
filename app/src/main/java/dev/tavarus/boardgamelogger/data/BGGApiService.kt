package dev.tavarus.boardgamelogger.data

import dev.tavarus.boardgamelogger.data.apimodels.BoardGameItems
import dev.tavarus.boardgamelogger.data.apimodels.CollectionItems
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface BGGApiService {
    @GET("collection")
    suspend fun getCollection(@Query("username") username: String): Response<CollectionItems> // Need to handle the 202 or whatever response for when the stuff isn't ready yet
    // Also, there's an excaped apostrophe thing in a title, might have to parse those

    @GET("thing")
    suspend fun getThing(@Query("id") id: String): BoardGameItems
}
