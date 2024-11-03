package com.dicoding.asclepius.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.HistoryIndicatedRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryIndicatedViewModel(private val repository: HistoryIndicatedRepository) : ViewModel() {

    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }

    fun insert(history: HistoryEntity) {
        viewModelScope.launch {
            repository.insert(history)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }

    fun getAllHistory(callback: (List<HistoryEntity>) -> Unit) {
        viewModelScope.launch {
            val historyList = repository.getAllHistory()
            callback(historyList)
        }
    }

}