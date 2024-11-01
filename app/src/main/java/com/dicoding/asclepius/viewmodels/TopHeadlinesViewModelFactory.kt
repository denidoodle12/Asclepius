package com.dicoding.asclepius.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.remote.TopHeadlinesRepository
import com.dicoding.asclepius.di.Injection

class TopHeadlinesViewModelFactory private constructor(private val repository: TopHeadlinesRepository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopHeadlinesViewModel::class.java)) {
            return TopHeadlinesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: TopHeadlinesViewModelFactory? = null
        fun getInstance(context: Context): TopHeadlinesViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: TopHeadlinesViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}