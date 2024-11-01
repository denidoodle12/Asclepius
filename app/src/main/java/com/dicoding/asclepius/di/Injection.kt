package com.dicoding.asclepius.di

import com.dicoding.asclepius.data.remote.TopHeadlinesRepository
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): TopHeadlinesRepository {
        val apiService = ApiConfig.getApiService()
        return TopHeadlinesRepository(apiService)
    }
}