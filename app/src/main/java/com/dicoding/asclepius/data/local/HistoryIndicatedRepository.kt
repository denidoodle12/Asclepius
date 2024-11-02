package com.dicoding.asclepius.data.local

import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.room.HistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryIndicatedRepository(private val historyDao: HistoryDao) {

    suspend fun insert(history: HistoryEntity) {
        withContext(Dispatchers.IO) {
            historyDao.insert(history)
        }
    }

    suspend fun getAllHistory() : List<HistoryEntity> {
        return withContext(Dispatchers.IO) {
            historyDao.getAllHistory()
        }
    }

    companion object {
        @Volatile
        private var instance: HistoryIndicatedRepository? = null
        fun getInstance(
            historyDao: HistoryDao
        ): HistoryIndicatedRepository =
            instance ?: synchronized(this) {
                instance ?: HistoryIndicatedRepository(historyDao)
            }.also { instance = it }
    }

}