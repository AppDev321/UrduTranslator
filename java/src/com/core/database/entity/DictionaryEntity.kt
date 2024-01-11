package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "a_Group")
data class DictionaryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int? = 0,
    @ColumnInfo("word")
    val word: String? = "",
    @ColumnInfo("meaning")
    val meaning: String? = "",
)

