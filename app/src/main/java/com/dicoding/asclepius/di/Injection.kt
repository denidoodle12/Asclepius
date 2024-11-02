package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.HistoryIndicatedRepository
import com.dicoding.asclepius.data.local.room.HistoryDatabase
import com.dicoding.asclepius.data.remote.TopHeadlinesRepository
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): TopHeadlinesRepository {
        val apiService = ApiConfig.getApiService()
        return TopHeadlinesRepository(apiService)
    }

    fun provideHistoryRepository(context: Context): HistoryIndicatedRepository {
        val database = HistoryDatabase.getDatabase(context)
        return HistoryIndicatedRepository.getInstance(database.historyDao())
    }
}