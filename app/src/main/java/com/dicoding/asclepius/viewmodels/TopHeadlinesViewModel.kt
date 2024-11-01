package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.TopHeadlinesRepository
import kotlinx.coroutines.launch

class TopHeadlinesViewModel(private val repository: TopHeadlinesRepository) : ViewModel() {
    val topHeadlines = repository.topHeadlines

    fun getTopHeadlines() {
        viewModelScope.launch {
            repository.getTopHeadlines()
        }
    }
}