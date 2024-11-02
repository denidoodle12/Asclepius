package com.dicoding.asclepius.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "HistoryIndicated")
@Parcelize
data class HistoryEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    val id:Long = 0,

    @field:ColumnInfo(name = "historyOfImage")
    val historyOfImage:String,

    @field:ColumnInfo(name = "historyOfLabel")
    val historyOfLabel: String,

    @field:ColumnInfo(name = "historyOfScore")
    val historyOfScore: String,
) : Parcelable
