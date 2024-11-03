package com.dicoding.asclepius.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(history: HistoryEntity)

    @Query("DELETE FROM HistoryIndicated")
    suspend fun deleteAllHistory()

    @Query("SELECT * FROM HistoryIndicated ORDER BY id DESC")
    suspend fun getAllHistory() : List<HistoryEntity>
}