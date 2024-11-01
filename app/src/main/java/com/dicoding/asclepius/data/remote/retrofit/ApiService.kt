package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.data.remote.response.TopHeadlinesUSResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlinesUS(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String
    ): TopHeadlinesUSResponse
}