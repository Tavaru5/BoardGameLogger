package dev.tavarus.boardgamelogger.data.network

import dev.tavarus.boardgamelogger.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

object BGGAuthHeader : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request =
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.bggApiKey}").build()
        return chain.proceed(request)
    }
}