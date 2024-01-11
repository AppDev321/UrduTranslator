package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val _id: Long = 0,
    val word_id: Int,
    val urdu_word: String,
    val eng_word: String,
    val is_urdu: Int
)
