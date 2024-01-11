
package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "a_Group")
data class DictionaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("word")
    val word: Int,
    @ColumnInfo("meaning")
    val meaning: String,
)

