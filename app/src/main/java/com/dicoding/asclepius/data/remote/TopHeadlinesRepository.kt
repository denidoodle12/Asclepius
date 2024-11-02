package com.dicoding.asclepius.data.remote

import com.dicoding.asclepius.data.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class TopHeadlinesRepository(
    private val apiService: ApiService
) {
    private val _topHeadlines = MutableLiveData<Result<List<ArticlesItem>>>()
    val topHeadlines: LiveData<Result<List<ArticlesItem>>> = _topHeadlines

    suspend fun getTopHeadlines() {
        _topHeadlines.postValue(Result.Loading)
        try {
            val response = apiService.getTopHeadlinesUS()
            _topHeadlines.postValue(Result.Success(response.articles))
        } catch (e: Exception) {
            _topHeadlines.postValue(Result.Error("Error: ${e.message}"))
        }
    }
}