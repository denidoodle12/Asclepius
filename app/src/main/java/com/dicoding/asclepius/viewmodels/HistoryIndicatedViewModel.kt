package com.dicoding.asclepius.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.HistoryIndicatedRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao
import kotlinx.coroutines.launch

class HistoryIndicatedViewModel(private val repository: HistoryIndicatedRepository) : ViewModel() {

    fun insert(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insert(history)
        }
    }

    fun getAllHistory(callback: (List<HistoryEntity>) -> Unit) {
        viewModelScope.launch {
            val historyList = repository.getAllHistory()
            callback(historyList)
        }
    }

}