package com.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "english_data")
data class LearnEntity(
    @PrimaryKey
    @ColumnInfo("_id")
    var id: Int? = 0,
    @ColumnInfo("category_name")
    var categoryName: String? = "",
    @ColumnInfo("urdu_words")
    var urduWords: String? = "",
    @ColumnInfo("english_words")
    var englishWords: String? = "",
    @ColumnInfo("transliteration")
    var transliteration: String? = "",
    @ColumnInfo("subCategory")
    var subCategory: String? = "",
    @ColumnInfo("voice")
    var voice:String? = ""

)