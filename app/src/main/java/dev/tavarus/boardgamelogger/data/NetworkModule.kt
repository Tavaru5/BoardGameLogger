package dev.tavarus.boardgamelogger.data

import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

private const val BASE_URL = "https://boardgamegeek.com/xmlapi2/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideTikXmlConverterFactory(): TikXmlConverterFactory {
        return TikXmlConverterFactory.create(
            TikXml
                .Builder()
                .exceptionOnUnreadXml(false)
                .build()
        )
    }

    @Provides
    @Singleton
    fun provideRetrofit(tikXmlConverterFactory: TikXmlConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(tikXmlConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): BGGApiService {
        return retrofit.create(BGGApiService::class.java)
    }

    @Provides
    fun provideGameRemoteDataSource(apiService: BGGApiService): GameRemoteDataSource {
        return GameRemoteDataSource(apiService)
    }

    @Provides
    fun provideGameListRepository(gameRemoteDataSource: GameRemoteDataSource): GameListRepository {
        return GameListDataRepository(gameRemoteDataSource)
    }

    @Provides
    fun provideBoardGameRepository(gameRemoteDataSource: GameRemoteDataSource): BoardGameRepository {
        return BoardGameDataRepository(gameRemoteDataSource)
    }

}