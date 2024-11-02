package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.TopHeadlinesUSResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesUS(
        @Query("q") q: String = "cancer",
        @Query("category") category: String = "health",
        @Query("language") country: String = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): TopHeadlinesUSResponse
}