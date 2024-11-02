package com.dicoding.asclepius.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.local.HistoryIndicatedRepository
import com.dicoding.asclepius.data.remote.TopHeadlinesRepository
import com.dicoding.asclepius.di.Injection

class HistoryIndicatedViewModelFactory private constructor(private val repository: HistoryIndicatedRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryIndicatedViewModel::class.java)) {
            return HistoryIndicatedViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HistoryIndicatedViewModelFactory? = null
        fun getInstance(context: Context): HistoryIndicatedViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: HistoryIndicatedViewModelFactory(Injection.provideHistoryRepository(context))
            }.also { instance = it }
    }
}