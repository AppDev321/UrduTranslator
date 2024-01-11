/*
* @AhsanKhan
* 3/31/22
* */


package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavouriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("_id")
    val id: Int,
    @ColumnInfo("word_id")
    val wordId: Int,
    @ColumnInfo("urdu_word")
    val urduWord: String,
    @ColumnInfo("eng_word")
    val engWord: Boolean,
    @ColumnInfo("is_urdu")
    val isUrdu: Int,
)

